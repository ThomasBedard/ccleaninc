package com.ccleaninc.cclean.employeessubdomain.businesslayer;

import com.ccleaninc.cclean.employeessubdomain.datalayer.Employee;
import com.ccleaninc.cclean.employeessubdomain.datalayer.EmployeeIdentifier;
import com.ccleaninc.cclean.employeessubdomain.datalayer.EmployeeRepository;
import com.ccleaninc.cclean.employeessubdomain.datamapperlayer.EmployeeRequestMapper;
import com.ccleaninc.cclean.employeessubdomain.datamapperlayer.EmployeeResponseMapper;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeRequestModel;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeResponseMapper employeeResponseMapper;

    @Mock
    private EmployeeRequestMapper employeeRequestMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeResponseModel employeeResponseModel;

    @BeforeEach
    void setUp() {
        // Create a sample Employee entity
        employee = Employee.builder()
                .id(1)
                .employeeIdentifier(
                    new EmployeeIdentifier("emp-1234-5678")
                )
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("555-1234")
                .isActive(true)
                .role("Technician")
                .build();

        // Create the corresponding EmployeeResponseModel
        employeeResponseModel = EmployeeResponseModel.builder()
                .employeeId("emp-1234-5678")
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("555-1234")
                .isActive(true)
                .role("Technician")
                .build();
    }

    @Test
    void getAllEmployees_ShouldReturnEmployees() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(employeeResponseMapper.entityToResponseModelList(List.of(employee)))
                .thenReturn(List.of(employeeResponseModel));

        // Act
        List<EmployeeResponseModel> response = employeeService.getAllEmployees();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("emp-1234-5678", response.get(0).getEmployeeId());
        assertEquals("Jane", response.get(0).getFirstName());
        assertEquals("Doe", response.get(0).getLastName());
        assertEquals("jane.doe@example.com", response.get(0).getEmail());
        assertEquals("555-1234", response.get(0).getPhoneNumber());
        assertTrue(response.get(0).getIsActive());
        assertEquals("Technician", response.get(0).getRole());
    }

    @Test
    void getAllEmployees_NoEmployeesFound_ShouldReturnEmptyList() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<EmployeeResponseModel> response = employeeService.getAllEmployees();

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void getEmployeeByEmployeeId_ShouldReturnEmployee() {
        // Arrange
        String employeeId = "emp-1234-5678";
        when(employeeRepository.findByEmployeeIdentifier_EmployeeId(employeeId))
                .thenReturn(Optional.of(employee));
        when(employeeResponseMapper.entityToResponseModel(employee))
                .thenReturn(employeeResponseModel);

        // Act
        EmployeeResponseModel response = employeeService.getEmployeeByEmployeeId(employeeId);

        // Assert
        assertNotNull(response);
        assertEquals("emp-1234-5678", response.getEmployeeId());
        assertEquals("Jane", response.getFirstName());
    }

    @Test
    void getEmployeeByEmployeeId_EmployeeNotFound_ShouldThrowNotFoundException() {
        // Arrange
        String employeeId = "emp-invalid";
        when(employeeRepository.findByEmployeeIdentifier_EmployeeId(employeeId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, 
            () -> employeeService.getEmployeeByEmployeeId(employeeId)
        );
    }

    @Test
    void searchEmployees_ShouldReturnEmployees() {
        // Arrange
        String searchTerm = "Jane";
        when(employeeRepository.searchByNameOrEmail(searchTerm))
                .thenReturn(List.of(employee));
        when(employeeResponseMapper.entityToResponseModelList(List.of(employee)))
                .thenReturn(List.of(employeeResponseModel));

        // Act
        List<EmployeeResponseModel> response = employeeService.searchEmployees(searchTerm);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Jane", response.get(0).getFirstName());
    }

    @Test
    void searchEmployees_NoEmployeesFound_ShouldReturnEmptyList() {
        // Arrange
        String searchTerm = "NonExistent";
        when(employeeRepository.searchByNameOrEmail(searchTerm))
                .thenReturn(Collections.emptyList());

        // Act
        List<EmployeeResponseModel> response = employeeService.searchEmployees(searchTerm);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void addEmployee_ShouldReturnCreatedEmployee() {
        // Arrange
        // Suppose your request model doesn't have the employeeId (auto-set)
        EmployeeRequestModel requestModel = EmployeeRequestModel.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("555-1234")
                .isActive(true)
                .role("Technician")
                .build();

        // The mapper converts EmployeeRequestModel to an Employee entity
        when(employeeRequestMapper.employeeModelToEntity(any(EmployeeRequestModel.class)))
                .thenReturn(employee);

        // The repository returns the same employee after saving
        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        // The response mapper converts the saved employee to EmployeeResponseModel
        when(employeeResponseMapper.entityToResponseModel(employee))
                .thenReturn(employeeResponseModel);

        // Act
        EmployeeResponseModel response = employeeService.addEmployee(requestModel);

        // Assert
        assertNotNull(response);
        assertEquals("emp-1234-5678", response.getEmployeeId());
    }

    @Test
    void updateEmployee_ShouldUpdateAndReturnEmployee() {
        // Arrange
        String employeeId = "emp-1234-5678";
        EmployeeRequestModel requestModel = EmployeeRequestModel.builder()
                .firstName("Janet")
                .lastName("Smith")
                .email("janet.smith@example.com")
                .phoneNumber("555-9876")
                .isActive(false)
                .role("Manager")
                .build();

        when(employeeRepository.findByEmployeeIdentifier_EmployeeId(employeeId))
                .thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        // The updated employee might have different fields
        // but in this simple test, we just return the same `employee`.
        // Realistically, you'd reflect updated fields here.
        when(employeeResponseMapper.entityToResponseModel(employee))
                .thenReturn(employeeResponseModel);

        // Act
        EmployeeResponseModel response = employeeService.updateEmployee(employeeId, requestModel);

        // Assert
        assertNotNull(response);
        // Expect the same employeeId
        assertEquals("emp-1234-5678", response.getEmployeeId());
    }

    @Test
    void deleteEmployee_ShouldDeleteEmployee() {
        // Arrange
        String employeeId = "emp-1234-5678";
        when(employeeRepository.findByEmployeeIdentifier_EmployeeId(employeeId))
                .thenReturn(Optional.of(employee));

        // Act & Assert
        assertDoesNotThrow(() -> employeeService.deleteEmployeeByEmployeeId(employeeId));
        // If no exception is thrown, the test passes
    }

    @Test
    void deleteEmployee_EmployeeNotFound_ShouldThrowNotFoundException() {
        // Arrange
        String employeeId = "emp-invalid";
        when(employeeRepository.findByEmployeeIdentifier_EmployeeId(employeeId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> employeeService.deleteEmployeeByEmployeeId(employeeId));
    }
}
