package com.ccleaninc.cclean.customerssubdomain.businesslayer;

import java.util.List;

import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerRequestModel;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerResponseModel;

public interface CustomerService {

    List<CustomerResponseModel> getAllCustomers();

    CustomerResponseModel getCustomerByCustomerId(String customerId);

    void deleteCustomerByCustomerId(String customerId);

    List<CustomerResponseModel> searchCustomers(String searchTerm);

    CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel);

    CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel);
    
}
