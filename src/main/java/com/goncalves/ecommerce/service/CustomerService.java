package com.goncalves.ecommerce.service;

import com.goncalves.ecommerce.entity.Customer;
import com.goncalves.ecommerce.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    //Independency injection via the constructor
    public CustomerService (CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //create costumer
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    //List all customers
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    //Get customer by id
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    //Partial customer update by id
    public Customer partialUpdateById(Long id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        if (updatedCustomer.getFirstName() != null) {
            existingCustomer.setFirstName(updatedCustomer.getFirstName());
        }
        if (updatedCustomer.getLastName() != null) {
            existingCustomer.setLastName(updatedCustomer.getLastName());
        }
        if (updatedCustomer.getPhone() != null) {
            existingCustomer.setPhone(updatedCustomer.getPhone());
        }
        if (updatedCustomer.getAddress() != null) {
            existingCustomer.setAddress(updatedCustomer.getAddress());
        }

        return customerRepository.save(existingCustomer);
    }

    //Delete customer by id (blocked if has orders)
    public void deleteById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        //Business rule: Cannot delete if there are orders.
        if (customer.getOrders() != null && !customer.getOrders().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer cannot be deleted because it has associated orders");
        }

        customerRepository.delete(customer);
    }
}