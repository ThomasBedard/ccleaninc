package com.ccleaninc.cclean.customerssubdomain.presentationlayer;

import com.ccleaninc.cclean.customerssubdomain.businesslayer.CustomerService;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {
    
    private final CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponseModel>> getAllCustomers() {
        List<CustomerResponseModel> customers = customerService.getAllCustomers();
        if (customers == null || customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(customers);
    }
}
