package com.perkflow.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.perkflow.customer.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Transactional
class CustomerPersistenceIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void persistsReloadsAndDemonstratesEntityLifecycle() {
        Customer customer = new Customer("Ada Lovelace", "ada.lovelace@example.com");

        // A newly constructed entity is transient.
        assertNull(customer.getId());
        assertFalse(entityManager.contains(customer));

        entityManager.persist(customer);
        assertTrue(entityManager.contains(customer)); // managed
        entityManager.flush(); // synchronize pending changes with PostgreSQL
        Long customerId = customer.getId();
        assertNotNull(customerId);

        entityManager.clear(); // detach everything from the persistence context
        assertFalse(entityManager.contains(customer));

        Customer reloaded = entityManager.find(Customer.class, customerId);
        assertNotNull(reloaded);
        assertTrue(entityManager.contains(reloaded)); // find returns a managed entity
        assertEquals("Ada Lovelace", reloaded.getName());
        assertEquals("ada.lovelace@example.com", reloaded.getEmail());

        entityManager.detach(reloaded);
        assertFalse(entityManager.contains(reloaded)); // detached

        Customer managedForRemoval = entityManager.find(Customer.class, customerId);
        assertNotNull(managedForRemoval);
        assertTrue(entityManager.contains(managedForRemoval));
        entityManager.remove(managedForRemoval); // removed state
        assertFalse(entityManager.contains(managedForRemoval));
        entityManager.flush(); // issue the DELETE
        assertNull(entityManager.find(Customer.class, customerId));
    }
}
