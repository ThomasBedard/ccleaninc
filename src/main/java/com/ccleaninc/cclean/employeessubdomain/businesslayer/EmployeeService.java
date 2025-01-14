package com.ccleaninc.cclean.employeessubdomain.businesslayer;

import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeRequestModel;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;


public interface EmployeeService {

    package com.ccleaninc.cclean.employeessubdomain.businesslayer;

import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeRequestModel;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;

import java.util.List;

public interface EmployeeService {
    List<EmployeeResponseModel> getAllEmployees();

    // 1. Search employees
    List<EmployeeResponseModel> searchEmployees(String searchTerm);

    // 2. Get employee by employeeId
    EmployeeResponseModel getEmployeeByEmployeeId(String employeeId);

    // 3. Delete employee by employeeId
    void deleteEmployeeByEmployeeId(String employeeId);

    // 4. Add new employee
    EmployeeResponseModel addEmployee(EmployeeRequestModel employeeRequestModel);

    // 5. Update existing employee
    EmployeeResponseModel updateEmployee(String employeeId, EmployeeRequestModel employeeRequestModel);
}

}
