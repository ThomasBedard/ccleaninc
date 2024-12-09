package com.ccleaninc.cclean.employeesubdomain.presentationlayer;


import com.ccleaninc.cclean.employeessubdomain.businesslayer.EmployeeService;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeController;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerUnitTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeResponseModel employeeResponseModel;

    @BeforeEach
    void setUp() {
        employeeResponseModel = EmployeeResponseModel.builder()
                .id("1")
                .firstName("Bill")
                .lastName("Burr")
                .email("bill@gmail.com")
                .phoneNumber("450-333-4444")
                .role("employee")
                .isActive(true)
                .build();
    }

    @Test
    void getAllEmployees_ShouldReturnEmployees() {
        // Arrange
        when(employeeService.getAllEmployees()).thenReturn(List.of(employeeResponseModel));

        // Act
        ResponseEntity<List<EmployeeResponseModel>> response = employeeController.getAllEmployees();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(employeeResponseModel, response.getBody().get(0));
    }

    @Test
    void getAllEmployees_NoEmployeesFound_ShouldReturnNotFound() {
        // Arrange
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<EmployeeResponseModel>> response = employeeController.getAllEmployees();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllEmployees_EmployeesFound_ShouldReturnOkWithEmployees() {
        // Arrange
        List<EmployeeResponseModel> mockEmployees = List.of(
                EmployeeResponseModel.builder()
                        .id("1")
                        .firstName("Employee 1")
                        .lastName("Doe")
                        .email("employee1@example.com")
                        .phoneNumber("123-456-7890")
                        .role("Manager")
                        .isActive(true)
                        .build(),
                EmployeeResponseModel.builder()
                        .id("2")
                        .firstName("Employee 2")
                        .lastName("Smith")
                        .email("employee2@example.com")
                        .phoneNumber("987-654-3210")
                        .role("Technician")
                        .isActive(false)
                        .build()
        );
        when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

        // Act
        ResponseEntity<List<EmployeeResponseModel>> response = employeeController.getAllEmployees();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(mockEmployees, response.getBody());
    }
}
