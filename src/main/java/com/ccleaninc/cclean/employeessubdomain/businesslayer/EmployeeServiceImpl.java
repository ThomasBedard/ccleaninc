package com.ccleaninc.cclean.employeessubdomain.businesslayer;

import com.ccleaninc.cclean.employeessubdomain.datalayer.Employee;
import com.ccleaninc.cclean.employeessubdomain.datalayer.EmployeeIdentifier;
import com.ccleaninc.cclean.employeessubdomain.datalayer.EmployeeRepository;
import com.ccleaninc.cclean.employeessubdomain.datamapperlayer.EmployeeRequestMapper;
import com.ccleaninc.cclean.employeessubdomain.datamapperlayer.EmployeeResponseMapper;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeRequestModel;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeResponseMapper employeeResponseMapper;
    private final EmployeeRequestMapper employeeRequestMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
            EmployeeResponseMapper employeeResponseMapper,
            EmployeeRequestMapper employeeRequestMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeResponseMapper = employeeResponseMapper;
        this.employeeRequestMapper = employeeRequestMapper;
    }

    /**
     * 1. Get all employees
     */
    @Override
    public List<EmployeeResponseModel> getAllEmployees() {
        return employeeResponseMapper.entityToResponseModelList(
                employeeRepository.findAll());
    }

    /**
     * 2. Search employees by first name, last name, or email (partial match)
     */
    @Override
    public List<EmployeeResponseModel> searchEmployees(String searchTerm) {
        return employeeResponseMapper.entityToResponseModelList(
                employeeRepository.searchByNameOrEmail(searchTerm));
    }

    /**
     * 3. Retrieve an employee by employeeId
     */
    @Override
    public EmployeeResponseModel getEmployeeByEmployeeId(String employeeId) {
        Employee foundEmployee = employeeRepository.findByEmployeeIdentifier_EmployeeId(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + employeeId));

        return employeeResponseMapper.entityToResponseModel(foundEmployee);
    }

    /**
     * 4. Delete employee by employeeId
     */
    @Override
    public void deleteEmployeeByEmployeeId(String employeeId) {
        Employee foundEmployee = employeeRepository.findByEmployeeIdentifier_EmployeeId(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + employeeId));
        employeeRepository.delete(foundEmployee);
    }

    /**
     * 5. Add a new employee
     */
    @Override
    public EmployeeResponseModel addEmployee(EmployeeRequestModel employeeRequestModel) {
        // Convert request model to entity
        Employee employee = employeeRequestMapper.employeeModelToEntity(employeeRequestModel);

        // Initialize new EmployeeIdentifier if using embedded IDs
        employee.setEmployeeIdentifier(new EmployeeIdentifier());

        // Save and map back to response model
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeResponseMapper.entityToResponseModel(savedEmployee);
    }

    /**
     * 6. Update an existing employee
     */
    @Override
    public EmployeeResponseModel updateEmployee(String employeeId, EmployeeRequestModel employeeRequestModel) {
        Employee existingEmployee = employeeRepository.findByEmployeeIdentifier_EmployeeId(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + employeeId));

        // Update the fields you want to allow changes to
        existingEmployee.setFirstName(employeeRequestModel.getFirstName());
        existingEmployee.setLastName(employeeRequestModel.getLastName());
        existingEmployee.setEmail(employeeRequestModel.getEmail());
        existingEmployee.setPhoneNumber(employeeRequestModel.getPhoneNumber());
        existingEmployee.setIsActive(employeeRequestModel.getIsActive());
        existingEmployee.setRole(employeeRequestModel.getRole());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeResponseMapper.entityToResponseModel(updatedEmployee);
    }

    @Override
    public EmployeeResponseModel getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            throw new NotFoundException("Employee not found for email: " + email);
        }
        return employeeResponseMapper.entityToResponseModel(employee);
    }
}
