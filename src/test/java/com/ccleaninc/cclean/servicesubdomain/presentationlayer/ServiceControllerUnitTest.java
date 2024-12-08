package com.ccleaninc.cclean.servicesubdomain.presentationlayer;

import com.ccleaninc.cclean.servicesubdomain.businesslayer.ServiceService;
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
                new ServiceResponseModel(1, "Service 1", "Description 1", BigDecimal.valueOf(100.00), true, "Category 1", 60),
                new ServiceResponseModel(2, "Service 2", "Description 2", BigDecimal.valueOf(200.00), false, "Category 2", 30)
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

}
