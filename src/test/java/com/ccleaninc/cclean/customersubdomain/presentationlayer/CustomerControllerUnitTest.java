package com.ccleaninc.cclean.customersubdomain.presentationlayer;

import com.ccleaninc.cclean.customerssubdomain.businesslayer.CustomerService;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerController;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerRequestModel;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerUnitTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerResponseModel customerResponseModel;
    private CustomerRequestModel customerRequestModel;

    @BeforeEach
    void setUp() {
        customerResponseModel = CustomerResponseModel.builder()
                .customerId("1234-5678-9101-1121")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@mail.com")
                .phoneNumber("123-456-7890")
                .companyName(null)
                .build();
    }

    @Test
    void getAllCustomers_ShouldReturnCustomers() {
    // Arrange
    when(customerService.getAllCustomers()).thenReturn(List.of(customerResponseModel));

    // Act
    ResponseEntity<List<CustomerResponseModel>> response = customerController.getAllCustomers();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals(customerResponseModel, response.getBody().get(0));
    }

    @Test
    void getAllCustomers_noCustomersFound_shouldReturnNotFound() {
    // Arrange
    when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());

    // Act
    ResponseEntity<List<CustomerResponseModel>> response = customerController.getAllCustomers();

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getCustomerByCustomerId_ShouldReturnCustomer() {
        // Arrange
        String customerId = "1234-5678-9101-1121";
        when(customerService.getCustomerByCustomerId(customerId)).thenReturn(customerResponseModel);

        // Act
        ResponseEntity<CustomerResponseModel> response = customerController.getCustomerByCustomerId(customerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerResponseModel, response.getBody());
    }

    @Test
    void getCustomerByCustomerId_CustomerNotFound_ShouldReturnNotFound() {
        // Arrange
        String customerId = "invalid-id";
        when(customerService.getCustomerByCustomerId(customerId)).thenThrow(new NotFoundException("Customer not found"));

        // Act
        ResponseEntity<CustomerResponseModel> response = customerController.getCustomerByCustomerId(customerId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addCustomer_ShouldCreateCustomer() {
        // Arrange
        when(customerService.addCustomer(customerRequestModel)).thenReturn(customerResponseModel);

        // Act
        ResponseEntity<CustomerResponseModel> response = customerController.addCustomer(customerRequestModel);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerResponseModel, response.getBody());
    }

    @Test
    void addCustomer_InvalidInput_ShouldReturnBadRequest() {
        // Arrange
        when(customerService.addCustomer(customerRequestModel)).thenThrow(new InvalidInputException("Invalid input"));

        // Act
        ResponseEntity<CustomerResponseModel> response = customerController.addCustomer(customerRequestModel);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateCustomer_ShouldUpdateCustomer() {
        // Arrange
        String customerId = "1234-5678-9101-1121";
        when(customerService.updateCustomer(customerId, customerRequestModel)).thenReturn(customerResponseModel);

        // Act
        ResponseEntity<CustomerResponseModel> response = customerController.updateCustomer(customerId, customerRequestModel);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerResponseModel, response.getBody());
    }

    @Test
    void updateCustomer_CustomerNotFound_ShouldReturnNotFound() {
        // Arrange
        String customerId = "invalid-id";
        when(customerService.updateCustomer(customerId, customerRequestModel)).thenThrow(new NotFoundException("Customer not found"));

        // Act
        ResponseEntity<CustomerResponseModel> response = customerController.updateCustomer(customerId, customerRequestModel);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer() {
        // Arrange
        String customerId = "1234-5678-9101-1121";

        // Act
        ResponseEntity<Void> response = customerController.deleteCustomer(customerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteCustomer_CustomerNotFound_ShouldReturnNotFound() {
    // Arrange
    String customerId = "invalid-id";
    doThrow(new NotFoundException("Customer not found"))
            .when(customerService).deleteCustomerByCustomerId(customerId);

    // Act
    ResponseEntity<Void> response = customerController.deleteCustomer(customerId);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void searchCustomers_ShouldReturnCustomers() {
        // Arrange
        String searchTerm = "John";
        when(customerService.searchCustomers(searchTerm)).thenReturn(List.of(customerResponseModel));

        // Act
        ResponseEntity<List<CustomerResponseModel>> response = customerController.searchCustomers(searchTerm);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(customerResponseModel, response.getBody().get(0));
    }

    @Test
    void searchCustomers_NoCustomersFound_ShouldReturnNotFound() {
        // Arrange
        String searchTerm = "Nonexistent";
        when(customerService.searchCustomers(searchTerm)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<CustomerResponseModel>> response = customerController.searchCustomers(searchTerm);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getCustomerByEmail_ShouldReturnCustomer() {
        String email = "john.doe@mail.com";
        when(customerService.getOrCreateCustomerByEmail(email)).thenReturn(customerResponseModel);

        ResponseEntity<CustomerResponseModel> response = customerController.getCustomerByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerResponseModel, response.getBody());
    }

    @Test
    void getCustomerByEmail_CustomerNotFound_ShouldReturnInternalServerError() {
        String email = "nonexistent@mail.com";
        when(customerService.getOrCreateCustomerByEmail(email)).thenThrow(new RuntimeException("Customer not found"));

        ResponseEntity<CustomerResponseModel> response = customerController.getCustomerByEmail(email);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


}
