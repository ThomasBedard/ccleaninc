package com.ccleaninc.cclean.servicesubdomain.datamapperlayer;

import com.ccleaninc.cclean.servicesubdomain.datalayer.Service;
import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceIdentifier;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceResponseMapperTest {

    private ServiceResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ServiceResponseMapper.class);
    }

    @Test
    void entityToResponseModel_shouldMapCorrectly() {
        // Arrange
        Service service = Service.builder()
                .serviceIdentifier(new ServiceIdentifier())  // Assuming UUID is auto-generated
                .title("Window Cleaning")
                .description("Exterior window cleaning service")
                .pricing(new BigDecimal("75.00"))
                .isAvailable(true)
                .category("Cleaning")
                .durationMinutes(60)
                .build();

        // Act
        ServiceResponseModel responseModel = mapper.entityToResponseModel(service);

        // Assert
        assertNotNull(responseModel.getServiceId());  // UUID generated
        assertEquals("Window Cleaning", responseModel.getTitle());
        assertEquals("Exterior window cleaning service", responseModel.getDescription());
        assertEquals(new BigDecimal("75.00"), responseModel.getPricing());
        assertTrue(responseModel.getIsAvailable());
        assertEquals("Cleaning", responseModel.getCategory());
        assertEquals(60, responseModel.getDurationMinutes());
    }

    @Test
    void entityToResponseModelList_shouldMapListCorrectly() {
        // Arrange
        Service service1 = Service.builder()
                .serviceIdentifier(new ServiceIdentifier())
                .title("Window Cleaning")
                .description("Exterior window cleaning service")
                .pricing(new BigDecimal("75.00"))
                .isAvailable(true)
                .category("Cleaning")
                .durationMinutes(60)
                .build();

        Service service2 = Service.builder()
                .serviceIdentifier(new ServiceIdentifier())
                .title("Carpet Cleaning")
                .description("Deep carpet cleaning service")
                .pricing(new BigDecimal("120.00"))
                .isAvailable(false)
                .category("Cleaning")
                .durationMinutes(90)
                .build();

        // Act
        List<ServiceResponseModel> responseModels = mapper.entityToResponseModelList(List.of(service1, service2));

        // Assert
        assertEquals(2, responseModels.size());

        assertEquals("Window Cleaning", responseModels.get(0).getTitle());
        assertEquals("Carpet Cleaning", responseModels.get(1).getTitle());
        assertEquals(new BigDecimal("75.00"), responseModels.get(0).getPricing());
        assertEquals(new BigDecimal("120.00"), responseModels.get(1).getPricing());
    }
}
