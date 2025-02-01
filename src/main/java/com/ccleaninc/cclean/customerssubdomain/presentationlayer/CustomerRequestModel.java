package com.ccleaninc.cclean.customerssubdomain.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class CustomerRequestModel {

    private String firstName;
    private String lastName;
    private String companyName;
    private String email;
    private String phoneNumber;
    private String address;
    
}
