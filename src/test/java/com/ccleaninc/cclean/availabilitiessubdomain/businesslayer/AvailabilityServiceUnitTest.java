package com.ccleaninc.cclean.availabilitiessubdomain.businesslayer;

import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Availability;
import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.AvailabilityRepository;
import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Shift;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceUnitTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private AvailabilityResponseMapper availabilityResponseMapper;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    private Availability availability;
    private AvailabilityResponseModel availabilityResponseModel;

    @BeforeEach
    void setUp() {
        LocalDateTime availableDate = LocalDateTime.parse("2025-02-15T08:00");
        availability = Availability.builder()
                .id(1)
                .employeeId("e1b2c3d4-f5g6-11ec-82a8-0242ac130000")
                .employeeFirstName("Alice")
                .employeeLastName("Johnson")
                .availableDate(availableDate)
                .shift(Shift.MORNING)
                .comments("Available for morning shifts")
                .build();

        availabilityResponseModel = AvailabilityResponseModel.builder()
                .id(1)
                .employeeId("e1b2c3d4-f5g6-11ec-82a8-0242ac130000")
                .employeeFirstName("Alice")
                .employeeLastName("Johnson")
                .availableDate(availableDate)
                .shift(Shift.MORNING)
                .comments("Available for morning shifts")
                .build();
    }

    @Test
    void getAllAvailabilities_shouldSucceed() {
        when(availabilityRepository.findAll()).thenReturn(List.of(availability));
        when(availabilityResponseMapper.entityToResponseModelList(List.of(availability))).thenReturn(List.of(availabilityResponseModel));

        List<AvailabilityResponseModel> availabilities = availabilityService.getAllAvailabilities();

        assertEquals(1, availabilities.size());
        assertEquals(availabilityResponseModel.getId(), availabilities.get(0).getId());
    }

    @Test
    void getAvailabilityByAvailabilityId_shouldSucceed() {
        when(availabilityRepository.findAvailabilityByAvailabilityIdentifier_AvailabilityId("e1b2c3d4-f5g6-11ec-82a8-0242ac130000"))
                .thenReturn(availability);
        when(availabilityResponseMapper.entityToResponseModel(availability)).thenReturn(availabilityResponseModel);

        AvailabilityResponseModel response = availabilityService.getAvailabilityByAvailabilityId("e1b2c3d4-f5g6-11ec-82a8-0242ac130000");

        assertEquals(availabilityResponseModel.getId(), response.getId());
        assertEquals(availabilityResponseModel.getEmployeeId(), response.getEmployeeId());
    }

    @Test
    void getAvailabilityByAvailabilityId_shouldThrowNotFoundException() {
        when(availabilityRepository.findAvailabilityByAvailabilityIdentifier_AvailabilityId("invalid-id")).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            availabilityService.getAvailabilityByAvailabilityId("invalid-id");
        });

        assertEquals("Availability with ID invalid-id was not found.", exception.getMessage());
    }

    @Test
    void createAvailability_shouldSucceed() {
        AvailabilityRequestModel requestModel = AvailabilityRequestModel.builder()
                .employeeId("e1b2c3d4-f5g6-11ec-82a8-0242ac130000")
                .employeeFirstName("Alice")
                .employeeLastName("Johnson")
                .availableDate(LocalDateTime.parse("2025-02-15T08:00"))
                .shift(Shift.MORNING)
                .comments("Available for morning shifts")
                .build();

        when(availabilityRepository.save(any(Availability.class))).thenReturn(availability);
        when(availabilityResponseMapper.entityToResponseModel(availability)).thenReturn(availabilityResponseModel);

        AvailabilityResponseModel response = availabilityService.createAvailability(requestModel);

        assertEquals(availabilityResponseModel.getId(), response.getId());
    }

    @Test
    void createAvailability_shouldThrowInvalidInputException() {
        AvailabilityRequestModel requestModel = null;

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            availabilityService.createAvailability(requestModel);
        });

        assertEquals("Availability request model cannot be null.", exception.getMessage());
    }

    @Test
    void deleteAvailabilityByAvailabilityId_shouldSucceed() {
        when(availabilityRepository.findAvailabilityByAvailabilityIdentifier_AvailabilityId("e1b2c3d4-f5g6-11ec-82a8-0242ac130000"))
                .thenReturn(availability);

        assertDoesNotThrow(() -> {
            availabilityService.deleteAvailabilityByAvailabilityId("e1b2c3d4-f5g6-11ec-82a8-0242ac130000");
        });
    }

    @Test
    void deleteAvailabilityByAvailabilityId_shouldThrowNotFoundException() {
        when(availabilityRepository.findAvailabilityByAvailabilityIdentifier_AvailabilityId("invalid-id")).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            availabilityService.deleteAvailabilityByAvailabilityId("invalid-id");
        });

        assertEquals("Availability with ID invalid-id was not found.", exception.getMessage());
    }
}

