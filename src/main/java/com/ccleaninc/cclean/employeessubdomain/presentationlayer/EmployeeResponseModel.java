package com.ccleaninc.cclean.employeessubdomain.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class EmployeeResponseModel {

    private String employeeId;      
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
    private Boolean isActive;
}

