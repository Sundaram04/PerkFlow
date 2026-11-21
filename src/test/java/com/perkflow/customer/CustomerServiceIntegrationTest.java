package com.perkflow.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.perkflow.customer.entity.Customer;
import com.perkflow.customer.repository.CustomerRepository;
import com.perkflow.customer.service.CustomerService;
import jakarta.persistence.OptimisticLockException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Long customerId;
    private String originalName;

    @BeforeEach
    void createCustomer() {
        originalName = "Transaction Test " + UUID.randomUUID();
        Customer customer = customerRepository.saveAndFlush(
                new Customer(originalName, "transaction-" + UUID.randomUUID() + "@example.com"));
        customerId = customer.getId();
    }

    @AfterEach
    void removeCustomer() {
        if (customerId != null) {
            customerRepository.deleteById(customerId);
        }
    }

    @Test
    void commitsRenameUsingDirtyCheckingWithoutCallingSave() {
        String updatedName = "Committed " + UUID.randomUUID();

        customerService.renameCustomer(customerId, updatedName);

        Customer reloaded = customerRepository.findById(customerId).orElseThrow();
        assertEquals(updatedName, reloaded.getName());
        assertEquals(1L, reloaded.getVersion());
    }

    @Test
    void rollsBackRenameWhenTheEnclosingTransactionFails() {
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);

        assertThrows(IllegalStateException.class, () -> transaction.execute(status -> {
            customerService.renameCustomer(customerId, "Must Roll Back");
            throw new IllegalStateException("Force the enclosing transaction to roll back");
        }));

        Customer reloaded = customerRepository.findById(customerId).orElseThrow();
        assertEquals(originalName, reloaded.getName());
        assertEquals(0L, reloaded.getVersion());
    }

    @Test
    void rejectsAStaleUpdateFromAnIndependentlyLoadedTransaction() throws Exception {
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        CountDownLatch bothTransactionsLoaded = new CountDownLatch(2);
        CountDownLatch firstTransactionCommitted = new CountDownLatch(1);
        AtomicReference<Customer> firstEntity = new AtomicReference<>();
        AtomicReference<Customer> staleEntity = new AtomicReference<>();

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Long> firstCommit = executor.submit(() -> {
                Long version = transaction.execute(status -> {
                    Customer customer = customerRepository.findById(customerId).orElseThrow();
                    firstEntity.set(customer);
                    bothTransactionsLoaded.countDown();
                    await(bothTransactionsLoaded);

                    customer.renameTo("First transaction wins");
                    customerRepository.flush();
                    return customer.getVersion();
                });

                firstTransactionCommitted.countDown();
                return version;
            });

            Future<?> staleCommit = executor.submit(() -> transaction.execute(status -> {
                Customer customer = customerRepository.findById(customerId).orElseThrow();
                staleEntity.set(customer);
                bothTransactionsLoaded.countDown();
                await(bothTransactionsLoaded);
                await(firstTransactionCommitted);

                customer.renameTo("Stale transaction loses");
                customerRepository.flush();
                return null;
            }));

            assertEquals(1L, firstCommit.get(10, TimeUnit.SECONDS));
            ExecutionException lockFailure = assertThrows(
                    ExecutionException.class,
                    () -> staleCommit.get(10, TimeUnit.SECONDS));

            assertTrue(hasOptimisticLockCause(lockFailure));
            assertNotSame(firstEntity.get(), staleEntity.get());
        }

        Customer committed = customerRepository.findById(customerId).orElseThrow();
        assertEquals("First transaction wins", committed.getName());
        assertEquals(1L, committed.getVersion());
    }

    private void await(CountDownLatch latch) {
        try {
            if (!latch.await(10, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Timed out waiting for concurrent transaction");
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while coordinating transactions", exception);
        }
    }

    private boolean hasOptimisticLockCause(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof OptimisticLockingFailureException
                    || current instanceof OptimisticLockException) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
