package com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer;

import com.ccleaninc.cclean.availabilitiessubdomain.businesslayer.AvailabilityService;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AvailabilityControllerUnitTest {

    @Mock
    private AvailabilityService availabilityService;

    @InjectMocks
    private AvailabilityController availabilityController;

    private AvailabilityResponseModel availabilityResponseModel;
    private AvailabilityRequestModel availabilityRequestModel;
    private Jwt mockJwt;

    @BeforeEach
    void setUp() {
        availabilityResponseModel = AvailabilityResponseModel.builder()
                .availabilityId("123e4567-e89b-12d3-a456-426614174000")
                .employeeId("emp-001")
                .employeeFirstName("John")
                .employeeLastName("Doe")
                .availableDate(LocalDateTime.now())
                .shift(com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Shift.MORNING)
                .comments("Available for morning shift")
                .build();

        availabilityRequestModel = AvailabilityRequestModel.builder()
                // Note: When using JWT-based creation, these fields are overwritten
                // by the service using the employee lookup
                .employeeId("emp-001")
                .employeeFirstName("John")
                .employeeLastName("Doe")
                .availableDate(LocalDateTime.now())
                .shift(com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Shift.MORNING)
                .comments("Available for morning shift")
                .build();

        // Create a mock Jwt and set the claim for email
        mockJwt = mock(Jwt.class);
        when(mockJwt.getClaim("https://ccleaninc.com/email")).thenReturn("john.doe@example.com");
    }

    @Test
    void getAllAvailabilities_ShouldSucceed() {
        when(availabilityService.getAllAvailabilities()).thenReturn(List.of(availabilityResponseModel));

        ResponseEntity<List<AvailabilityResponseModel>> response = availabilityController.getAllAvailabilities();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(availabilityResponseModel, response.getBody().get(0));
    }

    @Test
    void getAllAvailabilities_NoAvailabilitiesFound_ShouldReturnNotFound() {
        when(availabilityService.getAllAvailabilities()).thenReturn(List.of());

        ResponseEntity<List<AvailabilityResponseModel>> response = availabilityController.getAllAvailabilities();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createAvailability_ShouldSucceed() {
        // Stub the new method expected by the controller:
        when(availabilityService.createAvailabilityForEmployee("john.doe@example.com", availabilityRequestModel))
                .thenReturn(availabilityResponseModel);

        ResponseEntity<AvailabilityResponseModel> response = availabilityController
                .createAvailability(mockJwt, availabilityRequestModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(availabilityResponseModel, response.getBody());
    }

    @Test
    void createAvailability_InvalidInput_ShouldReturnBadRequest() {
        when(availabilityService.createAvailabilityForEmployee("john.doe@example.com", availabilityRequestModel))
                .thenThrow(new InvalidInputException("Invalid input"));

        ResponseEntity<AvailabilityResponseModel> response = availabilityController
                .createAvailability(mockJwt, availabilityRequestModel);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAvailabilityByAvailabilityId_ShouldSucceed() {
        when(availabilityService.getAvailabilityByAvailabilityId("123e4567-e89b-12d3-a456-426614174000"))
                .thenReturn(availabilityResponseModel);

        ResponseEntity<AvailabilityResponseModel> response = availabilityController
                .getAvailabilityByAvailabilityId("123e4567-e89b-12d3-a456-426614174000");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(availabilityResponseModel, response.getBody());
    }

    @Test
    void getAvailabilityByAvailabilityId_NotFound_ShouldReturnNotFound() {
        when(availabilityService.getAvailabilityByAvailabilityId("123e4567-e89b-12d3-a456-426614174000"))
                .thenThrow(new NotFoundException("Availability not found."));

        ResponseEntity<AvailabilityResponseModel> response = availabilityController
                .getAvailabilityByAvailabilityId("123e4567-e89b-12d3-a456-426614174000");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteAvailabilityByAvailabilityId_ShouldSucceed() {
        // For void methods, simply call the method (or verify afterward)
        ResponseEntity<Void> response = availabilityController
                .deleteAvailabilityByAvailabilityId("123e4567-e89b-12d3-a456-426614174000", mockJwt);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

//     @Test
//     void deleteAvailabilityByAvailabilityId_NotFound_ShouldReturnNotFound() {
//         doThrow(new NotFoundException("Availability not found."))
//                 .when(availabilityService)
//                 .deleteAvailabilityByAvailabilityId("123e4567-e89b-12d3-a456-426614174000");

//         ResponseEntity<Void> response = availabilityController
//                 .deleteAvailabilityByAvailabilityId("123e4567-e89b-12d3-a456-426614174000", mockJwt);

//         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//     }

    @Test
    void generateAvailabilitiesPdf_ShouldSucceed() {
        when(availabilityService.generateAvailabilitiesPdf()).thenReturn(new ByteArrayOutputStream());

        ResponseEntity<byte[]> response = availabilityController.generateAvailabilitiesPdf();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getMyAvailabilities_ShouldReturnAvailabilities() {
        String email = "john.doe@example.com";
        when(mockJwt.getClaim("https://ccleaninc.com/email")).thenReturn(email);

        when(availabilityService.getAvailabilitiesByEmployeeEmail(email))
                .thenReturn(List.of(availabilityResponseModel));

        ResponseEntity<List<AvailabilityResponseModel>> response = availabilityController
                .getMyAvailabilities(mockJwt);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(availabilityResponseModel, response.getBody().get(0));
    }
}
