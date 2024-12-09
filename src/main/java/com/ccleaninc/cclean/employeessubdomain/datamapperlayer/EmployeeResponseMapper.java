package com.ccleaninc.cclean.employeessubdomain.datamapperlayer;


import com.ccleaninc.cclean.employeessubdomain.datalayer.Employee;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeResponseMapper {

    List<EmployeeResponseModel> entityToResponseModelList(List<Employee> employees);
}
