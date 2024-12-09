package com.ccleaninc.cclean.employeessubdomain.businesslayer;

import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;


public interface EmployeeService {
    List<EmployeeResponseModel> getAllEmployees();
}
