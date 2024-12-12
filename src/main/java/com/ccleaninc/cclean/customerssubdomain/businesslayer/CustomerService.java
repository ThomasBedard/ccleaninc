package com.ccleaninc.cclean.customerssubdomain.businesslayer;

import java.util.List;

import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerResponseModel;

public interface CustomerService {

    List<CustomerResponseModel> getAllCustomers();
    
}
