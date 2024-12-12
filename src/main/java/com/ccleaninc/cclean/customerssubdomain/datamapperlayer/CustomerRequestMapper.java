package com.ccleaninc.cclean.customerssubdomain.datamapperlayer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ccleaninc.cclean.customerssubdomain.datalayer.Customer;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerRequestModel;

@Mapper(componentModel = "spring")
public interface CustomerRequestMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerIdentifier", ignore = true)
    Customer customerModelToEntity(CustomerRequestModel customerRequestModel);
}
