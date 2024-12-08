package com.ccleaninc.cclean.servicesubdomain.datalayer;

import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceRequestMapper;
import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceResponseMapper;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ServiceRepositoryTest {
//    @Autowired
//    private ServiceRepository serviceRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    private final ServiceRequestMapper requestMapper = Mappers.getMapper(ServiceRequestMapper.class);
//    private final ServiceResponseMapper responseMapper = Mappers.getMapper(ServiceResponseMapper.class);
//
//    private Service savedService1;
//    private Service savedService2;
//
//    @BeforeEach
//    void setUp() {
//        Service service1 = Service.builder()
//                .title("Service 1")
//                .description("Description 1")
//                .pricing(BigDecimal.valueOf(100.0))
//                .category("Category 1")
//                .durationMinutes(60)
//                .isAvailable(true)
//                .build();
//        savedService1 = serviceRepository.save(service1);
//
//        Service service2 = Service.builder()
//                .title("Service 2")
//                .description("Description 2")
//                .pricing(BigDecimal.valueOf(200.0))
//                .category("Category 2")
//                .durationMinutes(90)
//                .isAvailable(false)
//                .build();
//        savedService2 = entityManager.persist(service2);
//    }
//
//    @AfterEach
//    void tearDown() {
//        serviceRepository.deleteAll();
//        entityManager.clear();
//    }
//
//    @Test
//    void testRequestModelToEntityMapping() {
//        ServiceRequestModel requestModel = ServiceRequestModel.builder()
//                .title("Mapped Service")
//                .description("Mapped Description")
//                .pricing(BigDecimal.valueOf(150.0))
//                .category("Mapped Category")
//                .durationMinutes(45)
//                .build();
//
//        Service service = requestMapper.serviceModelToEntity(requestModel);
//
//        assertNotNull(service);
//        assertEquals(requestModel.getTitle(), service.getTitle());
//        assertEquals(requestModel.getDescription(), service.getDescription());
//        assertEquals(requestModel.getPricing(), service.getPricing());
//        assertEquals(requestModel.getCategory(), service.getCategory());
//        assertEquals(requestModel.getDurationMinutes(), service.getDurationMinutes());
//    }
//
//    @Test
//    void testEntityToResponseModelMapping() {
//        List<Service> services = List.of(savedService1, savedService2);
//
//        List<ServiceResponseModel> responseModels = responseMapper.entityToResponseModelList(services);
//
//        assertNotNull(responseModels);
//        assertEquals(2, responseModels.size());
//
//        ServiceResponseModel responseModel1 = responseModels.get(0);
//        assertEquals(savedService1.getId(), responseModel1.getId());
//        assertEquals(savedService1.getTitle(), responseModel1.getTitle());
//        assertEquals(savedService1.getDescription(), responseModel1.getDescription());
//        assertEquals(savedService1.getPricing(), responseModel1.getPricing());
//        assertEquals(savedService1.getCategory(), responseModel1.getCategory());
//        assertEquals(savedService1.getDurationMinutes(), responseModel1.getDurationMinutes());
//        assertTrue(responseModel1.getIsAvailable());
//
//        ServiceResponseModel responseModel2 = responseModels.get(1);
//        assertEquals(savedService2.getId(), responseModel2.getId());
//        assertEquals(savedService2.getTitle(), responseModel2.getTitle());
//        assertEquals(savedService2.getDescription(), responseModel2.getDescription());
//        assertEquals(savedService2.getPricing(), responseModel2.getPricing());
//        assertEquals(savedService2.getCategory(), responseModel2.getCategory());
//        assertEquals(savedService2.getDurationMinutes(), responseModel2.getDurationMinutes());
//        assertFalse(responseModel2.getIsAvailable());
//    }


}
