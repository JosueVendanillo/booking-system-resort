package com.bluebell.project.controller;

import com.bluebell.project.model.CustomerInformation;
import com.bluebell.project.repository.CustomerInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
//@CrossOrigin(origins = "*") // React frontend
public class CustomerController {

    @Autowired
    private CustomerInformationRepository customerRepository;

    @GetMapping
    public List<CustomerInformation> getAllCustomers() {
        return customerRepository.findAll();
    }

    // ✅ Get a single customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerInformation> getCustomerById(@PathVariable Long id) {
        Optional<CustomerInformation> customer = customerRepository.findById(id);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Create a new customer
    @PostMapping
    public CustomerInformation createCustomer(@RequestBody CustomerInformation customer) {
        return customerRepository.save(customer);
    }

    // ✅ Update an existing customer
    @PutMapping("/{id}")
    public ResponseEntity<CustomerInformation> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerInformation customerDetails) {

        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFullname(customerDetails.getFullname());
                    customer.setEmail(customerDetails.getEmail());
                    customer.setContactNumber(customerDetails.getContactNumber());
                    customer.setGender(customerDetails.getGender()); // ✅ aligned with frontend
                    CustomerInformation updated = customerRepository.save(customer);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Delete a customer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(existing -> {
                    customerRepository.delete(existing);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
