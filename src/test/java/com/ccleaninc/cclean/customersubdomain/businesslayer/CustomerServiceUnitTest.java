package com.ccleaninc.cclean.customersubdomain.businesslayer;

import com.ccleaninc.cclean.customerssubdomain.businesslayer.CustomerServiceImpl;
import com.ccleaninc.cclean.customerssubdomain.datalayer.Customer;
import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerIdentifier;
import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerRepository;
import com.ccleaninc.cclean.customerssubdomain.datamapperlayer.CustomerRequestMapper;
import com.ccleaninc.cclean.customerssubdomain.datamapperlayer.CustomerResponseMapper;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerRequestModel;
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

    @Mock
    private CustomerRequestMapper customerRequestMapper;

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

        //  when(customerRequestMapper.customerModelToEntity(any(CustomerRequestModel.class))).thenReturn(customer);

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

    
    @Test
    void getCustomerByCustomerId_ShouldReturnCustomer() {
        // Arrange
        String customerId = "1234-5678-9101-1121";
        when(customerRepository.findByCustomerIdentifier_CustomerId(customerId)).thenReturn(java.util.Optional.of(customer));
        when(customerResponseMapper.entityToResponseModel(customer)).thenReturn(customerResponseModel);

        // Act
        CustomerResponseModel response = customerService.getCustomerByCustomerId(customerId);

        // Assert
        assertNotNull(response);
        assertEquals(customerResponseModel.getCustomerId(), response.getCustomerId());
    }

    @Test
    void getCustomerByCustomerId_CustomerNotFound_ShouldThrowNotFoundException() {
        // Arrange
        String customerId = "invalid-id";
        when(customerRepository.findByCustomerIdentifier_CustomerId(customerId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> customerService.getCustomerByCustomerId(customerId));
    }

    @Test
    void searchCustomers_ShouldReturnCustomers() {
        // Arrange
        String searchTerm = "John";
        when(customerRepository.searchByNameOrCompany(searchTerm)).thenReturn(List.of(customer));
        when(customerResponseMapper.entityToResponseModelList(List.of(customer))).thenReturn(List.of(customerResponseModel));

        // Act
        List<CustomerResponseModel> response = customerService.searchCustomers(searchTerm);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(customerResponseModel.getFirstName(), response.get(0).getFirstName());
    }

    @Test
    void searchCustomers_NoCustomersFound_ShouldReturnEmptyList() {
        // Arrange
        String searchTerm = "Nonexistent";
        when(customerRepository.searchByNameOrCompany(searchTerm)).thenReturn(Collections.emptyList());

        // Act
        List<CustomerResponseModel> response = customerService.searchCustomers(searchTerm);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

//     @Test
//     void addCustomer_ShouldReturnCreatedCustomer() {
//         // Arrange
//         when(customerRepository.save(any(Customer.class))).thenReturn(customer);
//         when(customerResponseMapper.entityToResponseModel(customer)).thenReturn(customerResponseModel);

//         // Act
//         CustomerResponseModel response = customerService.addCustomer(CustomerRequestModel.builder()
//                 .firstName("John")
//                 .lastName("Doe")
//                 .email("john.doe@mail.com")
//                 .phoneNumber("123-456-7890")
//                 .build());

//         // Assert
//         assertNotNull(response);
//         assertEquals(customerResponseModel.getCustomerId(), response.getCustomerId());
//     }

    @Test
    void updateCustomer_ShouldUpdateAndReturnCustomer() {
        // Arrange
        String customerId = "1234-5678-9101-1121";
        when(customerRepository.findByCustomerIdentifier_CustomerId(customerId)).thenReturn(java.util.Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerResponseMapper.entityToResponseModel(customer)).thenReturn(customerResponseModel);

        // Act
        CustomerResponseModel response = customerService.updateCustomer(customerId, CustomerRequestModel.builder()
                .firstName("Updated Name")
                .email("updated.email@mail.com")
                .phoneNumber("123-456-0000")
                .build());

        // Assert
        assertNotNull(response);
        assertEquals(customerResponseModel.getCustomerId(), response.getCustomerId());
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer() {
        // Arrange
        String customerId = "1234-5678-9101-1121";
        when(customerRepository.findByCustomerIdentifier_CustomerId(customerId)).thenReturn(java.util.Optional.of(customer));

        // Act
        customerService.deleteCustomerByCustomerId(customerId);

        // Assert
        // No exception should be thrown
    }

    @Test
    void deleteCustomer_CustomerNotFound_ShouldThrowNotFoundException() {
        // Arrange
        String customerId = "invalid-id";
        when(customerRepository.findByCustomerIdentifier_CustomerId(customerId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> customerService.deleteCustomerByCustomerId(customerId));
    }

}
