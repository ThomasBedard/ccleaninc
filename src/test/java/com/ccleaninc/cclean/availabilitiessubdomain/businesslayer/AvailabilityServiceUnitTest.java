package com.ccleaninc.cclean.availabilitiessubdomain.businesslayer;

import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Availability;
import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.AvailabilityIdentifier;
import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.AvailabilityRepository;
import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Shift;
import com.ccleaninc.cclean.availabilitiessubdomain.datamapperlayer.AvailabilityRequestMapper;
import com.ccleaninc.cclean.availabilitiessubdomain.datamapperlayer.AvailabilityResponseMapper;
import com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer.AvailabilityRequestModel;
import com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer.AvailabilityResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceUnitTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private AvailabilityResponseMapper availabilityResponseMapper;

    @Mock
    private AvailabilityRequestMapper availabilityRequestMapper;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    private Availability availability;
    private AvailabilityResponseModel availabilityResponseModel;

    @BeforeEach
    void setUp() {
        availability = Availability.builder()
                .availabilityIdentifier(new AvailabilityIdentifier("123e4567-e89b-12d3-a456-426614174000"))
                .employeeId("emp-001")
                .employeeFirstName("John")
                .employeeLastName("Doe")
                .availableDate(LocalDateTime.now())
                .shift(Shift.MORNING)
                .comments("Available for morning shift")
                .build();

        availabilityResponseModel = AvailabilityResponseModel.builder()
                .availabilityId("123e4567-e89b-12d3-a456-426614174000")
                .employeeId("emp-001")
                .employeeFirstName("John")
                .employeeLastName("Doe")
                .availableDate(LocalDateTime.now())
                .shift(Shift.MORNING)
                .comments("Available for morning shift")
                .build();
    }

    @Test
    void getAllAvailabilities_ShouldReturnAvailabilities() {
        // Arrange
        when(availabilityRepository.findAll()).thenReturn(List.of(availability));
        when(availabilityResponseMapper.entityToResponseModelList(List.of(availability))).thenReturn(List.of(availabilityResponseModel));

        // Act
        List<AvailabilityResponseModel> response = availabilityService.getAllAvailabilities();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(availabilityResponseModel.getAvailabilityId(), response.get(0).getAvailabilityId());
    }

    @Test
    void getAllAvailabilities_NoAvailabilitiesFound_ShouldReturnEmptyList() {
        // Arrange
        when(availabilityRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<AvailabilityResponseModel> response = availabilityService.getAllAvailabilities();

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void getAvailabilityByAvailabilityId_ShouldReturnAvailability() {
        // Arrange
        String availabilityId = "123e4567-e89b-12d3-a456-426614174000";
        when(availabilityRepository.findAvailabilityByAvailabilityIdentifier_AvailabilityId(availabilityId)).thenReturn(availability);
        when(availabilityResponseMapper.entityToResponseModel(availability)).thenReturn(availabilityResponseModel);

        // Act
        AvailabilityResponseModel response = availabilityService.getAvailabilityByAvailabilityId(availabilityId);

        // Assert
        assertNotNull(response);
        assertEquals(availabilityResponseModel.getAvailabilityId(), response.getAvailabilityId());
    }

    @Test
    void getAvailabilityByAvailabilityId_NotFound_ShouldThrowNotFoundException() {
        // Arrange
        String availabilityId = "123e4567-e89b-12d3-a456-426614174001";
        when(availabilityRepository.findAvailabilityByAvailabilityIdentifier_AvailabilityId(availabilityId)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> availabilityService.getAvailabilityByAvailabilityId(availabilityId));
    }

    @Test
    void createAvailability_ShouldReturnCreatedAvailability() {
        // Arrange
        AvailabilityRequestModel requestModel = AvailabilityRequestModel.builder()
                .employeeId("emp-001")
                .employeeFirstName("John")
                .employeeLastName("Doe")
                .availableDate(LocalDateTime.now())
                .shift(Shift.MORNING)
                .comments("Available for morning shift")
                .build();

        when(availabilityRepository.save(any(Availability.class))).thenReturn(availability);
        when(availabilityResponseMapper.entityToResponseModel(availability)).thenReturn(availabilityResponseModel);

        // Act
        AvailabilityResponseModel response = availabilityService.createAvailability(requestModel);

        // Assert
        assertNotNull(response);
        assertEquals(availabilityResponseModel.getAvailabilityId(), response.getAvailabilityId());
    }

    @Test
    void createAvailability_InvalidInput_ShouldThrowInvalidInputException() {
        // Arrange
        AvailabilityRequestModel requestModel = AvailabilityRequestModel.builder()
                .employeeId(null)
                .build();

        // Act & Assert
        assertThrows(InvalidInputException.class, () -> availabilityService.createAvailability(requestModel));
    }

    @Test
    void deleteAvailabilityByAvailabilityId_ShouldSucceed() {
        // Arrange
        String availabilityId = "123e4567-e89b-12d3-a456-426614174000";
        when(availabilityRepository.findAvailabilityByAvailabilityIdentifier_AvailabilityId(availabilityId)).thenReturn(availability);

        // Act
        availabilityService.deleteAvailabilityByAvailabilityId(availabilityId);

        // Assert
        // No exception should be thrown
    }

    @Test
    void deleteAvailabilityByAvailabilityId_NotFound_ShouldThrowNotFoundException() {
        // Arrange
        String availabilityId = "123e4567-e89b-12d3-a456-426614174001";
        when(availabilityRepository.findAvailabilityByAvailabilityIdentifier_AvailabilityId(availabilityId)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> availabilityService.deleteAvailabilityByAvailabilityId(availabilityId));
    }

    @Test
    void generateAvailabilitiesPdf_ShouldSucceed() {
        // Arrange
        when(availabilityRepository.findAll()).thenReturn(List.of(availability));

        // Act
        ByteArrayOutputStream pdfData = availabilityService.generateAvailabilitiesPdf();

        // Assert
        assertNotNull(pdfData);
    }
}