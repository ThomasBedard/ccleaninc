package com.ccleaninc.cclean.customerssubdomain.datamapperlayer;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ccleaninc.cclean.customerssubdomain.datalayer.Customer;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerResponseModel;

@Mapper(componentModel = "spring")
public interface CustomerResponseMapper {

    @Mapping(expression = "java(customer.getCustomerIdentifier().getCustomerId())", target = "customerId")
    CustomerResponseModel entityToResponseModel(Customer customer);

    List<CustomerResponseModel> entityToResponseModelList(List<Customer> customers);
}
