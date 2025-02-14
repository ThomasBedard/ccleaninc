package com.ccleaninc.cclean.servicesubdomain.presentationlayer;

import com.ccleaninc.cclean.servicesubdomain.businesslayer.ServiceService;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceControllerUnitTest {

    @Mock
    private ServiceService serviceService;

    @InjectMocks
    private ServiceController serviceController;
    private ServiceResponseModel serviceResponseModel;
    private ServiceRequestModel serviceRequestModel;

    @BeforeEach
    void setUp() {
        serviceResponseModel = ServiceResponseModel.builder()
                .id(1)
                .serviceId("7cb4e475-b787-11ef-94fe-0242ac1a0003")
                .title("Test Service")
                .description("Test Description")
                .pricing(new BigDecimal(100))
                .isAvailable(true)
                .category("Test Category")
                .durationMinutes(30)
                .build();
    }

    @Test
    void getAllServices_ShouldReturnServices(){
        when(serviceService.getAllServices()).thenReturn(List.of(serviceResponseModel));

        ResponseEntity<List<ServiceResponseModel>> response = serviceController.getAllServices();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(serviceResponseModel, response.getBody().get(0));
    }

    @Test
    void getAllServices_noServicesFound_shouldReturnNotFound(){
        when(serviceService.getAllServices()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ServiceResponseModel>> response = serviceController.getAllServices();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllServices_servicesFound_shouldReturnOkWithServices() {
        // Arrange
        List<ServiceResponseModel> mockServices = List.of(
                // Pass 9 parameters (including image) and use Boolean.TRUE/Boolean.FALSE
                new ServiceResponseModel(
                        1,
                        "7cb4e475-b787-11ef-94fe-0242ac1a0003",
                        "Service 1",
                        "Description 1",
                        BigDecimal.valueOf(100.00),
                        Boolean.TRUE,          // Instead of true
                        "Category 1",
                        60,
                        null                   // image = null (or some base64 string)
                ),
                new ServiceResponseModel(
                        2,
                        "7cb4e475-b787-11ef-94fe-0242ac1a0003",
                        "Service 2",
                        "Description 2",
                        BigDecimal.valueOf(200.00),
                        Boolean.FALSE,         // Instead of false
                        "Category 2",
                        30,
                        null
                )
        );

        when(serviceService.getAllServices()).thenReturn(mockServices);

        // Act
        ResponseEntity<List<ServiceResponseModel>> response = serviceController.getAllServices();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(mockServices, response.getBody());
    }

    @Test
    void getServiceByServiceId_serviceFound_shouldReturnOkWithService() {
        // Arrange
        String serviceId = "12345678-1234-1234-1234-123456789012";
        when(serviceService.getServiceByServiceId(serviceId)).thenReturn(serviceResponseModel);

        // Act
        ResponseEntity<ServiceResponseModel> response = serviceController.getServiceByServiceId(serviceId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(serviceResponseModel, response.getBody());
    }

    @Test
    void getServiceByServiceId_serviceNotFound_shouldReturnNotFound() {
        // Arrange
        String serviceId = "12345678-1234-1234-1234-123456789012";
        when(serviceService.getServiceByServiceId(serviceId)).thenThrow(new NotFoundException("Service not found"));

        // Act
        ResponseEntity<ServiceResponseModel> response = serviceController.getServiceByServiceId(serviceId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteServiceByServiceId_serviceFound_shouldReturnOk() {
        // Arrange
        String serviceId = "12345678-1234-1234-1234-123456789012";

        // Act
        ResponseEntity<Void> response = serviceController.deleteServiceByServiceId(serviceId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteServiceByServiceId_serviceNotFound_shouldReturnNotFound() {
        // Arrange
        String serviceId = "12345678-1234-1234-1234-123456789012";
        doThrow(new NotFoundException("Service not found"))
                .when(serviceService).deleteServiceByServiceId(serviceId);

        // Act
        ResponseEntity<Void> response = serviceController.deleteServiceByServiceId(serviceId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void searchServiceByTitle_serviceFound_shouldReturnOkWithServices() {
        // Arrange
        String title = "Test Service";
        List<ServiceResponseModel> mockServices = List.of(
                // Pass 9 parameters (including image) and use Boolean.TRUE/Boolean.FALSE
                new ServiceResponseModel(
                        1,
                        "7cb4e475-b787-11ef-94fe-0242ac1a0003",
                        "Service 1",
                        "Description 1",
                        BigDecimal.valueOf(100.00),
                        Boolean.TRUE,          // Instead of true
                        "Category 1",
                        60,
                        null                   // image = null (or some base64 string)
                ),
                new ServiceResponseModel(
                        2,
                        "7cb4e475-b787-11ef-94fe-0242ac1a0003",
                        "Service 2",
                        "Description 2",
                        BigDecimal.valueOf(200.00),
                        Boolean.FALSE,         // Instead of false
                        "Category 2",
                        30,
                        null
                )
        );

        when(serviceService.searchServiceByServiceTitle(title)).thenReturn(mockServices);

        // Act
        ResponseEntity<List<ServiceResponseModel>> response = serviceController.searchServiceByTitle(title);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(mockServices, response.getBody());
    }

    @Test
    void searchServiceByTitle_serviceNotFound_shouldReturnNotFound() {
        // Arrange
        String title = "Test Service";
        when(serviceService.searchServiceByServiceTitle(title)).thenThrow(new NotFoundException("Service not found"));

        // Act
        ResponseEntity<List<ServiceResponseModel>> response = serviceController.searchServiceByTitle(title);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void searchServiceByTitle_invalidInput_shouldReturnBadRequest() {
        // Arrange
        String title = "";
        doThrow(new InvalidInputException("Service title cannot be null or empty."))
                .when(serviceService).searchServiceByServiceTitle(title);

        // Act
        ResponseEntity<List<ServiceResponseModel>> response = serviceController.searchServiceByTitle(title);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addService_serviceAdded_shouldReturnCreatedWithService() {
        // Arrange
        serviceRequestModel = ServiceRequestModel.builder()
                .title("Test Service")
                .description("Test Description")
                .pricing(BigDecimal.valueOf(100.00))
                .category("Test Category")
                .durationMinutes(30)
                .build();
        when(serviceService.addService(serviceRequestModel)).thenReturn(serviceResponseModel);

        // Act
        ResponseEntity<ServiceResponseModel> response = serviceController.addService(serviceRequestModel);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(serviceResponseModel, response.getBody());
    }

    @Test
    void addService_serviceNotAdded_shouldReturnBadRequest() {
        // Arrange
        serviceRequestModel = ServiceRequestModel.builder()
                .title("Test Service")
                .description("Test Description")
                .pricing(BigDecimal.valueOf(100.00))
                .category("Test Category")
                .durationMinutes(30)
                .build();
        when(serviceService.addService(serviceRequestModel)).thenReturn(null);

        // Act
        ResponseEntity<ServiceResponseModel> response = serviceController.addService(serviceRequestModel);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateService_serviceUpdated_shouldReturnOkWithService() {
        // Arrange
        String serviceId = "12345678-1234-1234-1234-123456789012";
        serviceRequestModel = ServiceRequestModel.builder()
                .title("Test Service")
                .description("Test Description")
                .pricing(BigDecimal.valueOf(100.00))
                .category("Test Category")
                .durationMinutes(30)
                .build();
        when(serviceService.updateService(serviceId, serviceRequestModel)).thenReturn(serviceResponseModel);

        // Act
        ResponseEntity<ServiceResponseModel> response = serviceController.updateService(serviceId, serviceRequestModel);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(serviceResponseModel, response.getBody());
    }

    @Test
    void updateService_serviceNotFound_shouldReturnNotFound() {
        // Arrange
        String serviceId = "12345678-1234-1234-1234-123456789012";
        serviceRequestModel = ServiceRequestModel.builder()
                .title("Test Service")
                .description("Test Description")
                .pricing(BigDecimal.valueOf(100.00))
                .category("Test Category")
                .durationMinutes(30)
                .build();
        when(serviceService.updateService(serviceId, serviceRequestModel)).thenThrow(new NotFoundException("Service not found"));

        // Act
        ResponseEntity<ServiceResponseModel> response = serviceController.updateService(serviceId, serviceRequestModel);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateService_invalidInput_shouldReturnBadRequest() {
        // Arrange
        String serviceId = "12345678-1234-1234-1234-123456789012";
        serviceRequestModel = ServiceRequestModel.builder()
                .title("Test Service")
                .description("Test Description")
                .pricing(BigDecimal.valueOf(100.00))
                .category("Test Category")
                .durationMinutes(30)
                .build();
        doThrow(new InvalidInputException("Service request model cannot be null."))
                .when(serviceService).updateService(serviceId, serviceRequestModel);

        // Act
        ResponseEntity<ServiceResponseModel> response = serviceController.updateService(serviceId, serviceRequestModel);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
