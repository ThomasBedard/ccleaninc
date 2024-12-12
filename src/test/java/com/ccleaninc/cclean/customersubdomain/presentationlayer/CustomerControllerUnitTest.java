package com.ccleaninc.cclean.customersubdomain.presentationlayer;

import com.ccleaninc.cclean.customerssubdomain.businesslayer.CustomerService;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerController;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerRequestModel;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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


}
