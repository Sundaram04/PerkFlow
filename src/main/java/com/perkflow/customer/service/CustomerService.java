package com.perkflow.customer.service;

import com.perkflow.customer.entity.Customer;
import com.perkflow.customer.repository.CustomerRepository;
import com.perkflow.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void renameCustomer(Long customerId, String newName) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id " + customerId + " was not found"));

        customer.renameTo(newName);
    }
}
