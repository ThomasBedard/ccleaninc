package com.ccleaninc.cclean.employeessubdomain.presentationlayer;

import com.ccleaninc.cclean.employeessubdomain.businesslayer.EmployeeService;
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
        employeeResponseModel = EmployeeResponseModel.builder()
                .employeeId("emp-1234-5678")
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("555-1234")
                .role("Technician")
                .isActive(true)
                .build();

        employeeRequestModel = EmployeeRequestModel.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("555-1234")
                .role("Technician")
                .isActive(true)
                .build();
    }

    @Test
    void getAllEmployees_ShouldReturnOkWithEmployees() {
        when(employeeService.getAllEmployees()).thenReturn(List.of(employeeResponseModel));
        ResponseEntity<List<EmployeeResponseModel>> response = employeeController.getAllEmployees();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(employeeResponseModel, response.getBody().get(0));
    }

    @Test
    void getAllEmployees_NoEmployeesFound_ShouldReturnNotFound() {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());
        ResponseEntity<List<EmployeeResponseModel>> response = employeeController.getAllEmployees();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getEmployeeByEmployeeId_ShouldReturnEmployee() {
        String employeeId = "emp-1234-5678";
        when(employeeService.getEmployeeByEmployeeId(employeeId)).thenReturn(employeeResponseModel);
        ResponseEntity<EmployeeResponseModel> response = employeeController.getEmployeeByEmployeeId(employeeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employeeResponseModel, response.getBody());
    }

    @Test
    void getEmployeeByEmployeeId_NotFound_ShouldReturnNotFound() {
        String invalidId = "emp-9999";
        when(employeeService.getEmployeeByEmployeeId(invalidId)).thenThrow(new NotFoundException("Employee not found"));
        ResponseEntity<EmployeeResponseModel> response = employeeController.getEmployeeByEmployeeId(invalidId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addEmployee_ShouldCreateEmployee() {
        when(employeeService.addEmployee(employeeRequestModel)).thenReturn(employeeResponseModel);
        ResponseEntity<EmployeeResponseModel> response = employeeController.addEmployee(employeeRequestModel);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employeeResponseModel, response.getBody());
    }

    @Test
    void addEmployee_InvalidInput_ShouldReturnBadRequest() {
        when(employeeService.addEmployee(employeeRequestModel)).thenThrow(new InvalidInputException("Invalid input"));
        ResponseEntity<EmployeeResponseModel> response = employeeController.addEmployee(employeeRequestModel);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateEmployee_ShouldUpdateAndReturnEmployee() {
        String employeeId = "emp-1234-5678";
        when(employeeService.updateEmployee(employeeId, employeeRequestModel)).thenReturn(employeeResponseModel);
        ResponseEntity<EmployeeResponseModel> response = employeeController.updateEmployee(employeeId, employeeRequestModel);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employeeResponseModel, response.getBody());
    }

    @Test
    void updateEmployee_EmployeeNotFound_ShouldReturnNotFound() {
        String invalidId = "emp-9999";
        when(employeeService.updateEmployee(invalidId, employeeRequestModel)).thenThrow(new NotFoundException("Employee not found"));
        ResponseEntity<EmployeeResponseModel> response = employeeController.updateEmployee(invalidId, employeeRequestModel);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteEmployee_ShouldDeleteEmployee() {
    // Arrange
    String employeeId = "emp-1234-5678";
    doNothing().when(employeeService).deleteEmployeeByEmployeeId(employeeId); // Mock service call

    // Act
    ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()); // Assert 204 No Content
}


    @Test
    void deleteEmployee_EmployeeNotFound_ShouldReturnNotFound() {
        String invalidId = "emp-9999";
        doThrow(new NotFoundException("Employee not found")).when(employeeService).deleteEmployeeByEmployeeId(invalidId);
        ResponseEntity<Void> response = employeeController.deleteEmployee(invalidId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void searchEmployees_ShouldReturnEmployees() {
        String searchTerm = "Jane";
        when(employeeService.searchEmployees(searchTerm)).thenReturn(List.of(employeeResponseModel));
        ResponseEntity<List<EmployeeResponseModel>> response = employeeController.searchEmployees(searchTerm);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(employeeResponseModel, response.getBody().get(0));
    }

    @Test
    void searchEmployees_NoEmployeesFound_ShouldReturnNotFound() {
        String searchTerm = "NotExist";
        when(employeeService.searchEmployees(searchTerm)).thenReturn(Collections.emptyList());
        ResponseEntity<List<EmployeeResponseModel>> response = employeeController.searchEmployees(searchTerm);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
