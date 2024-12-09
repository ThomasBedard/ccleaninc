package com.ccleaninc.cclean.employeessubdomain.businesslayer;

import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import com.ccleaninc.cclean.employeessubdomain.datalayer.EmployeeRepository;
import com.ccleaninc.cclean.employeessubdomain.datamapperlayer.EmployeeRequestMapper;
import com.ccleaninc.cclean.employeessubdomain.datamapperlayer.EmployeeResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{

    private EmployeeRepository employeeRepository;
    private EmployeeResponseMapper employeeResponseMapper;
    private EmployeeRequestMapper employeeRequestMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeResponseMapper employeeResponseMapper, EmployeeRequestMapper employeeRequestMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeResponseMapper = employeeResponseMapper;
        this.employeeRequestMapper = employeeRequestMapper;
    }

    @Override
    public List<EmployeeResponseModel> getAllEmployees() {
        return employeeResponseMapper.entityToResponseModelList(employeeRepository.findAll());
    }

}
