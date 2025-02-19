package com.ccleaninc.cclean.employeessubdomain.presentationlayer;

import com.ccleaninc.cclean.employeessubdomain.businesslayer.EmployeeService;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/employees")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

    private final EmployeeService employeeService;

    // Get all employees
    @GetMapping
    public ResponseEntity<List<EmployeeResponseModel>> getAllEmployees() {
        List<EmployeeResponseModel> employees = employeeService.getAllEmployees();
        if (employees == null || employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(employees);
    }

    // Get an employee by employee ID
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseModel> getEmployeeByEmployeeId(@PathVariable String employeeId) {
        try {
            EmployeeResponseModel employee = employeeService.getEmployeeByEmployeeId(employeeId);
            return ResponseEntity.ok(employee);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Add a new employee
    @PostMapping
    public ResponseEntity<EmployeeResponseModel> addEmployee(@RequestBody EmployeeRequestModel employeeRequestModel) {
        try {
            EmployeeResponseModel newEmployee = employeeService.addEmployee(employeeRequestModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId) {
        try {
            employeeService.deleteEmployeeByEmployeeId(employeeId);
            return ResponseEntity.noContent().build(); // Returns 204 No Content
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Returns 404 if not found
        }
    }

    // Search employees by term
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponseModel>> searchEmployees(@RequestParam String searchTerm) {
        List<EmployeeResponseModel> employees = employeeService.searchEmployees(searchTerm);
        if (employees == null || employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(employees);
    }

    // ----------------------------
    // New endpoints for Employee Profile
    // ----------------------------

    // Get employee profile for the currently authenticated employee (using JWT)
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<EmployeeResponseModel> getEmployeeProfile(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("https://ccleaninc.com/email");
        try {
            EmployeeResponseModel employee = employeeService.getEmployeeByEmail(email);
            return ResponseEntity.ok(employee);
        } catch (NotFoundException e) {
            // If no profile is found, return 404 or handle auto-provisioning as desired
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Create a new employee profile for the current user (if not already created)
    @PostMapping("/profile")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<EmployeeResponseModel> createEmployeeProfile(@AuthenticationPrincipal Jwt jwt,
            @RequestBody EmployeeRequestModel requestModel) {
        String email = jwt.getClaim("https://ccleaninc.com/email");
        // Overwrite the email in the request with the email from the JWT to ensure
        // consistency.
        EmployeeRequestModel profileRequest = EmployeeRequestModel.builder()
                .firstName(requestModel.getFirstName())
                .lastName(requestModel.getLastName())
                .email(email)
                .phoneNumber(requestModel.getPhoneNumber())
                .role(requestModel.getRole())
                .isActive(true)
                .build();
        try {
            EmployeeResponseModel newEmployee = employeeService.addEmployee(profileRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Update the current employee's profile
    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<EmployeeResponseModel> updateEmployeeProfile(@AuthenticationPrincipal Jwt jwt,
            @RequestBody EmployeeRequestModel requestModel) {
        String email = jwt.getClaim("https://ccleaninc.com/email");
        try {
            // First, find the employee by email
            EmployeeResponseModel existingEmployee = employeeService.getEmployeeByEmail(email);
            // Use the existing employeeId and update details (overwrite email with JWT's
            // email)
            EmployeeRequestModel updatedRequest = EmployeeRequestModel.builder()
                    .firstName(requestModel.getFirstName())
                    .lastName(requestModel.getLastName())
                    .email(email)
                    .phoneNumber(requestModel.getPhoneNumber())
                    .role(requestModel.getRole())
                    .isActive(requestModel.getIsActive())
                    .build();
            EmployeeResponseModel updatedEmployee = employeeService.updateEmployee(existingEmployee.getEmployeeId(),
                    updatedRequest);
            return ResponseEntity.ok(updatedEmployee);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
