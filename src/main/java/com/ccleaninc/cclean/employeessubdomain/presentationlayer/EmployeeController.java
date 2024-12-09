package com.ccleaninc.cclean.employeessubdomain.presentationlayer;


import com.ccleaninc.cclean.employeessubdomain.businesslayer.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
