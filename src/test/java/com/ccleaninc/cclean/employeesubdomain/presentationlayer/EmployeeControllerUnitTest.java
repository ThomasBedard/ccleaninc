package com.ccleaninc.cclean.employeesubdomain.presentationlayer;

import com.ccleaninc.cclean.employeessubdomain.businesslayer.EmployeeService;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeController;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeRequestModel;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerUnitTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeRequestModel employeeRequestModel;
    private EmployeeResponseModel employeeResponseModel;

    @BeforeEach
    void setUp() {
        // Sample EmployeeResponseModel
        employeeResponseModel = EmployeeResponseModel.builder()
                .employeeId("emp-1234-5678")
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("555-1234")
                .role("Technician")
                .isActive(true)
                .build();

        // Sample EmployeeRequestModel
        employeeRequestModel = EmployeeRequestModel.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("555-1234")
                .role("Technician")
                .isActive(true)
                .build();
    }

    /* 
     * 1. getAllEmployees Tests
     */
    @Test
    void getAllEmployees_EmployeesFound_ShouldReturnOkWithEmployees() {
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

    /* 
     * 2. getEmployeeByEmployeeId Tests
     */
    @Test
    void getEmployeeByEmployeeId_ShouldReturnEmployee() {
        // Arrange
        String employeeId = "emp-1234-5678";
        when(employeeService.getEmployeeByEmployeeId(employeeId)).thenReturn(employeeResponseModel);

        // Act
        ResponseEntity<EmployeeResponseModel> response = employeeController.getEmployeeByEmployeeId(employeeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employeeResponseModel, response.getBody());
    }

    @Test
    void getEmployeeByEmployeeId_NotFound_ShouldReturnNotFound() {
        // Arrange
        String invalidId = "emp-9999";
        when(employeeService.getEmployeeByEmployeeId(invalidId))
                .thenThrow(new NotFoundException("Employee not found"));

        // Act
        ResponseEntity<EmployeeResponseModel> response = employeeController.getEmployeeByEmployeeId(invalidId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /* 
     * 3. addEmployee Tests
     */
    @Test
    void addEmployee_ShouldCreateEmployee() {
        // Arrange
        when(employeeService.addEmployee(employeeRequestModel)).thenReturn(employeeResponseModel);

        // Act
        ResponseEntity<EmployeeResponseModel> response = employeeController.addEmployee(employeeRequestModel);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employeeResponseModel, response.getBody());
    }

    @Test
    void addEmployee_InvalidInput_ShouldReturnBadRequest() {
        // Arrange
        when(employeeService.addEmployee(employeeRequestModel))
                .thenThrow(new InvalidInputException("Invalid input"));

        // Act
        ResponseEntity<EmployeeResponseModel> response = employeeController.addEmployee(employeeRequestModel);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /* 
     * 4. updateEmployee Tests
     */
    @Test
    void updateEmployee_ShouldUpdateAndReturnEmployee() {
        // Arrange
        String employeeId = "emp-1234-5678";
        when(employeeService.updateEmployee(employeeId, employeeRequestModel)).thenReturn(employeeResponseModel);

        // Act
        ResponseEntity<EmployeeResponseModel> response = employeeController.updateEmployee(employeeId, employeeRequestModel);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employeeResponseModel, response.getBody());
    }

    @Test
    void updateEmployee_EmployeeNotFound_ShouldReturnNotFound() {
        // Arrange
        String invalidId = "emp-9999";
        when(employeeService.updateEmployee(invalidId, employeeRequestModel))
                .thenThrow(new NotFoundException("Employee not found"));

        // Act
        ResponseEntity<EmployeeResponseModel> response = employeeController.updateEmployee(invalidId, employeeRequestModel);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /* 
     * 5. deleteEmployee Tests
     */
    @Test
    void deleteEmployee_ShouldDeleteEmployee() {
        // Arrange
        String employeeId = "emp-1234-5678";

        // Act
        ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // No body needed, just OK status
    }

    @Test
    void deleteEmployee_EmployeeNotFound_ShouldReturnNotFound() {
        // Arrange
        String invalidId = "emp-9999";
        doThrow(new NotFoundException("Employee not found"))
                .when(employeeService).deleteEmployeeByEmployeeId(invalidId);

        // Act
        ResponseEntity<Void> response = employeeController.deleteEmployee(invalidId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /* 
     * 6. searchEmployees Tests
     */
    @Test
    void searchEmployees_ShouldReturnEmployees() {
        // Arrange
        String searchTerm = "Jane";
        when(employeeService.searchEmployees(searchTerm))
                .thenReturn(List.of(employeeResponseModel));

        // Act
        ResponseEntity<List<EmployeeResponseModel>> response =
                employeeController.searchEmployees(searchTerm);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(employeeResponseModel, response.getBody().get(0));
    }

    @Test
    void searchEmployees_NoEmployeesFound_ShouldReturnNotFound() {
        // Arrange
        String searchTerm = "NotExist";
        when(employeeService.searchEmployees(searchTerm))
                .thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<EmployeeResponseModel>> response =
                employeeController.searchEmployees(searchTerm);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
