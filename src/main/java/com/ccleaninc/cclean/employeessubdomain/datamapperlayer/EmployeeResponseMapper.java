package com.ccleaninc.cclean.employeessubdomain.datamapperlayer;


import com.ccleaninc.cclean.employeessubdomain.datalayer.Employee;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeResponseMapper {

    // List<EmployeeResponseModel> entityToResponseModelList(List<Employee> employees);

    // Map from a single Employee entity to an EmployeeResponseModel
    // We extract the 'employeeId' from the embedded EmployeeIdentifier
    @Mapping(source = "employeeIdentifier.employeeId", target = "employeeId")
    EmployeeResponseModel entityToResponseModel(Employee employee);

    // Map from a list of Employee entities to a list of EmployeeResponseModels
    List<EmployeeResponseModel> entityToResponseModelList(List<Employee> employees);
}
