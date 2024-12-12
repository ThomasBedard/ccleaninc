package com.ccleaninc.cclean.customersubdomain.businesslayer;

import com.ccleaninc.cclean.customerssubdomain.businesslayer.CustomerServiceImpl;
import com.ccleaninc.cclean.customerssubdomain.datalayer.Customer;
import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerIdentifier;
import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerRepository;
import com.ccleaninc.cclean.customerssubdomain.datamapperlayer.CustomerResponseMapper;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerResponseModel;

import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTest {

     @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerResponseMapper customerResponseMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;
    private Customer customer;
    private CustomerResponseModel customerResponseModel;

    @BeforeEach
    void setUp() {
        // Initialize a sample Customer entity
        customer = Customer.builder()
                .id(1)
                .customerIdentifier(new CustomerIdentifier("1234-5678-9101-1121"))
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@mail.com")
                .phoneNumber("123-456-7890")
                .build();

        // Initialize the corresponding CustomerResponseModel
        customerResponseModel = CustomerResponseModel.builder()
                .customerId("1234-5678-9101-1121")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@mail.com")
                .phoneNumber("123-456-7890")
                .build();
    }
    
    @Test
    void getAllCustomers_ShouldReturnCustomers() {
    // Arrange
    when(customerRepository.findAll()).thenReturn(List.of(customer));
    when(customerResponseMapper.entityToResponseModelList(List.of(customer))).thenReturn(List.of(customerResponseModel));

    // Act
    List<CustomerResponseModel> response = customerService.getAllCustomers();

    // Assert
    assertNotNull(response);
    assertEquals(1, response.size());
    assertEquals(customerResponseModel.getCustomerId(), response.get(0).getCustomerId());
    assertEquals(customerResponseModel.getFirstName(), response.get(0).getFirstName());
    assertEquals(customerResponseModel.getLastName(), response.get(0).getLastName());
    assertEquals(customerResponseModel.getEmail(), response.get(0).getEmail());
    assertEquals(customerResponseModel.getPhoneNumber(), response.get(0).getPhoneNumber());
    }

    @Test
    void getAllCustomers_NoCustomersFound_ShouldReturnEmptyList() {
    // Arrange
    when(customerRepository.findAll()).thenReturn(Collections.emptyList());

    // Act
    List<CustomerResponseModel> response = customerService.getAllCustomers();

    // Assert
    assertNotNull(response);
    assertTrue(response.isEmpty());
    }

}
