package com.perkflow.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.perkflow.customer.entity.Customer;
import com.perkflow.customer.repository.CustomerRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Transactional
class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void savesCustomerAndFindsItByGeneratedId() {
        Customer saved = customerRepository.save(newCustomer("Id Lookup"));

        assertNotNull(saved.getId());
        Customer found = customerRepository.findById(saved.getId()).orElseThrow();

        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getName(), found.getName());
        assertEquals(saved.getEmail(), found.getEmail());
    }

    @Test
    void findsCustomerByEmailAndChecksExistence() {
        Customer saved = customerRepository.save(newCustomer("Email Lookup"));

        assertEquals(saved.getId(), customerRepository.findByEmail(saved.getEmail()).orElseThrow().getId());
        assertTrue(customerRepository.existsByEmail(saved.getEmail()));
    }

    @Test
    void findsCustomersByName() {
        String name = "Name Lookup " + UUID.randomUUID();
        Customer saved = customerRepository.save(new Customer(name, uniqueEmail()));

        List<Customer> found = customerRepository.findByName(name);

        assertEquals(1, found.size());
        assertEquals(saved.getId(), found.getFirst().getId());
    }

    @Test
    void returnsEmptyWhenEmailDoesNotExist() {
        assertTrue(customerRepository.findByEmail(uniqueEmail()).isEmpty());
    }

    @Test
    void deletesCustomerByGeneratedId() {
        Customer saved = customerRepository.save(newCustomer("Delete Lookup"));
        Long id = saved.getId();

        customerRepository.deleteById(id);
        customerRepository.flush();

        assertFalse(customerRepository.existsById(id));
    }

    private Customer newCustomer(String namePrefix) {
        return new Customer(namePrefix + " " + UUID.randomUUID(), uniqueEmail());
    }

    private String uniqueEmail() {
        return "repository-" + UUID.randomUUID() + "@example.com";
    }
}
