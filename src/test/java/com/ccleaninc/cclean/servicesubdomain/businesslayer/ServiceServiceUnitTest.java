package com.ccleaninc.cclean.servicesubdomain.businesslayer;

import com.ccleaninc.cclean.servicesubdomain.datalayer.Service;
import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceRepository;
import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceResponseMapper;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceUnitTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceResponseMapper serviceResponseMapper;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    private Service service;

    private ServiceResponseModel serviceResponseModel;

    @BeforeEach
    void setUp() {
        // Initialize the Service entity
        service = Service.builder()
                .id(1)
                .title("Test Service")
                .description("Test Description")
                .pricing(BigDecimal.valueOf(100.00))
                .category("Test Category")
                .durationMinutes(30)
                .isAvailable(true)
                .build();

        // Initialize the corresponding ServiceResponseModel
        serviceResponseModel = ServiceResponseModel.builder()
                .id(1)
                .title("Test Service")
                .description("Test Description")
                .pricing(BigDecimal.valueOf(100.00))
                .category("Test Category")
                .durationMinutes(30)
                .isAvailable(true)
                .build();
    }

    @Test
    void getAllServices_ShouldSucceed() {
        // Arrange
        when(serviceRepository.findAll()).thenReturn(List.of(service));
        when(serviceResponseMapper.entityToResponseModelList(List.of(service))).thenReturn(List.of(serviceResponseModel));

        // Act
        List<ServiceResponseModel> response = serviceService.getAllServices();

        // Assert
        assertEquals(1, response.size());
        assertEquals(serviceResponseModel.getId(), response.get(0).getId());
        assertEquals(serviceResponseModel.getTitle(), response.get(0).getTitle());
        assertEquals(serviceResponseModel.getDescription(), response.get(0).getDescription());
        assertEquals(serviceResponseModel.getPricing(), response.get(0).getPricing());
        assertEquals(serviceResponseModel.getCategory(), response.get(0).getCategory());
        assertEquals(serviceResponseModel.getDurationMinutes(), response.get(0).getDurationMinutes());
        assertTrue(response.get(0).getIsAvailable());
    }

    @Test
    void getAllServices_noServicesFound_shouldReturnEmptyList() {
        // Arrange
        when(serviceRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ServiceResponseModel> response = serviceService.getAllServices();

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void getServiceByServiceId_ShouldSucceed() {
        // Arrange
        when(serviceRepository.findServiceByServiceIdentifier_ServiceId("7cb4e475-b787-11ef-94fe-0242ac1a0006")).thenReturn(service);
        when(serviceResponseMapper.entityToResponseModel(service)).thenReturn(serviceResponseModel);

        // Act
        ServiceResponseModel response = serviceService.getServiceByServiceId("7cb4e475-b787-11ef-94fe-0242ac1a0006");

        // Assert
        assertEquals(serviceResponseModel.getId(), response.getId());
        assertEquals(serviceResponseModel.getServiceId(), response.getServiceId());
        assertEquals(serviceResponseModel.getTitle(), response.getTitle());
        assertEquals(serviceResponseModel.getDescription(), response.getDescription());
        assertEquals(serviceResponseModel.getPricing(), response.getPricing());
        assertEquals(serviceResponseModel.getCategory(), response.getCategory());
        assertEquals(serviceResponseModel.getDurationMinutes(), response.getDurationMinutes());
        assertTrue(response.getIsAvailable());
    }

    @Test
    void getServiceByServiceId_invalidServiceId_shouldThrowInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> serviceService.getServiceByServiceId("invalid"));
    }

    @Test
    void deleteServiceByServiceId_ShouldSucceed() {
        // Arrange
        String validServiceId = "123e4567-e89b-12d3-a456-426614174000";
        when(serviceRepository.findServiceByServiceIdentifier_ServiceId(validServiceId)).thenReturn(service);

        // Act
        serviceService.deleteServiceByServiceId(validServiceId);

        // Assert
        // No exception should be thrown
    }

    @Test
    void deleteServiceByServiceId_invalidServiceId_shouldThrowInvalidInputException() {
        // Act & Assert
        assertThrows(InvalidInputException.class, () -> serviceService.deleteServiceByServiceId("invalid"));
    }



}




