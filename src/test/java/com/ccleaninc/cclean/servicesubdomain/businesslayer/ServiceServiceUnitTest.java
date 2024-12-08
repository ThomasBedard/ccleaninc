package com.ccleaninc.cclean.servicesubdomain.businesslayer;

import com.ccleaninc.cclean.servicesubdomain.datalayer.Service;
import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceRepository;
import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceResponseMapper;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
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


}




