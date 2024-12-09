package com.ccleaninc.cclean.employeessubdomain.datamapperlayer;


import com.ccleaninc.cclean.employeessubdomain.datalayer.Employee;

import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeIdentifier", ignore = true)
    Employee employeeModelToEntity(EmployeeRequestModel employeeRequestModel);

    // @Mapping(target = "isActive", source = "isActive")
    // Employee employeeModelToEntity(EmployeeRequestModel employeeRequestModel);

}
