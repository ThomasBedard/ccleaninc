package com.ccleaninc.cclean.employeessubdomain.presentationlayer;

import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import com.ccleaninc.cclean.employeessubdomain.businesslayer.EmployeeService;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

    private final EmployeeService employeeService;



    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeResponseModel>> getAllEmployees() {
        List<EmployeeResponseModel> employees = employeeService.getAllEmployees();
        if(employees == null || employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(employees);
    }

        // Get a employee by employee ID
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseModel> getEmployeeByEmployeeId(@PathVariable String employeeId) {
        try {
            EmployeeResponseModel employee = employeeService.getEmployeeByEmployeeId(employeeId);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Add a new employee
    @PostMapping
    public ResponseEntity<EmployeeResponseModel> addEmployee(@RequestBody EmployeeRequestModel employeeRequestModel) {
        try {
            EmployeeResponseModel newEmployee = employeeService.addEmployee(employeeRequestModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Update an existing employee
    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseModel> updateEmployee(
            @PathVariable String employeeId,
            @RequestBody EmployeeRequestModel employeeRequestModel) {
        try {
            EmployeeResponseModel updatedEmployee = employeeService.updateEmployee(employeeId, employeeRequestModel);
            return ResponseEntity.ok(updatedEmployee);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delete a employee by employee ID
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId) {
        try {
            employeeService.deleteEmployeeByEmployeeId(employeeId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Search employees 
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponseModel>> searchEmployees(@RequestParam String searchTerm) {
        List<EmployeeResponseModel> employees = employeeService.searchEmployees(searchTerm);
        if (employees == null || employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(employees);
    }
}
