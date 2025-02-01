package com.ccleaninc.cclean.customerssubdomain.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class CustomerResponseModel {

    private String customerId;    
    private String firstName;     // optional if individual
    private String lastName;      // optional if individual
    private String companyName;   // optional if business
    private String email;
    private String phoneNumber;
    private String address;
}
