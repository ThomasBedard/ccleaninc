package com.ccleaninc.cclean.employeessubdomain.datamapperlayer;


import com.ccleaninc.cclean.employeessubdomain.datalayer.Employee;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeResponseMapper {

    @Mapping(expression = "java(employee.getEmployeeIdentifier().getEmployeeId())", target = "employeeId")
    EmployeeResponseModel entityToResponseModel(Employee employee);

    // Map from a list of Employee entities to a list of EmployeeResponseModels
    List<EmployeeResponseModel> entityToResponseModelList(List<Employee> employees);
}
