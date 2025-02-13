package com.ccleaninc.cclean.customerssubdomain.presentationlayer;

import com.ccleaninc.cclean.customerssubdomain.businesslayer.CustomerService;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/customers")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {
    
    private final CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<CustomerResponseModel>> getAllCustomers() {
        List<CustomerResponseModel> customers = customerService.getAllCustomers();
        if (customers == null || customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(customers);
    }

    // Get a customer by customer ID
    @GetMapping("/{customerId}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<CustomerResponseModel> getCustomerByCustomerId(@PathVariable String customerId) {
        try {
            CustomerResponseModel customer = customerService.getCustomerByCustomerId(customerId);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Add a new customer
    @PostMapping
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<CustomerResponseModel> addCustomer(@RequestBody CustomerRequestModel customerRequestModel) {
        try {
            CustomerResponseModel newCustomer = customerService.addCustomer(customerRequestModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Update an existing customer
    @PutMapping("/{customerId}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<CustomerResponseModel> updateCustomer(
            @PathVariable String customerId,
            @RequestBody CustomerRequestModel customerRequestModel) {
        try {
            CustomerResponseModel updatedCustomer = customerService.updateCustomer(customerId, customerRequestModel);
            return ResponseEntity.ok(updatedCustomer);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delete a customer by customer ID
    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        try {
            customerService.deleteCustomerByCustomerId(customerId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Search customers by name or company
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<CustomerResponseModel>> searchCustomers(@RequestParam String searchTerm) {
        List<CustomerResponseModel> customers = customerService.searchCustomers(searchTerm);
        if (customers == null || customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/byEmail")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<CustomerResponseModel> getCustomerByEmail(@RequestParam String email) {
        try {
            // Must use getOrCreateCustomerByEmail here
            CustomerResponseModel customer = customerService.getOrCreateCustomerByEmail(email);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}

