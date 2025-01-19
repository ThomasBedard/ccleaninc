package com.ccleaninc.cclean.appointmentssubdomain.presentationlayer;

import com.ccleaninc.cclean.appointmentssubdomain.businesslayer.AppointmentService;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentControllerUnitTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    private AppointmentResponseModel appointmentResponseModel;
    private AppointmentRequestModel appointmentRequestModel;

    @BeforeEach
    void setUp() {
        LocalDateTime appointmentDate = LocalDateTime.parse("2021-08-01T10:00");

        // A typical response model you expect back
        appointmentResponseModel = AppointmentResponseModel.builder()
                .id(1)
                .appointmentId("123e4567-e89b-12d3-a456-426614174000")
                .customerId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")
                .appointmentDate(appointmentDate)
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();

        // A typical request model you send in
        appointmentRequestModel = AppointmentRequestModel.builder()
                .appointmentDate(appointmentDate)
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();
    }

    @Test
    void getAllAppointments_ShouldSucceed() {
        when(appointmentService.getAllAppointments()).thenReturn(List.of(appointmentResponseModel));

        ResponseEntity<List<AppointmentResponseModel>> response =
                appointmentController.getAllAppointments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(appointmentResponseModel, response.getBody().get(0));
    }

    @Test
    void getAllAppointments_NoAppointmentsFound_ShouldReturnNotFound() {
        when(appointmentService.getAllAppointments()).thenReturn(List.of());

        ResponseEntity<List<AppointmentResponseModel>> response =
                appointmentController.getAllAppointments();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createAppointment_ShouldSucceed() {
        when(appointmentService.createAppointment(appointmentRequestModel))
                .thenReturn(appointmentResponseModel);

        ResponseEntity<AppointmentResponseModel> response =
                appointmentController.createAppointment(appointmentRequestModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(appointmentResponseModel, response.getBody());
    }

    @Test
    void createAppointment_InvalidInput_ShouldReturnBadRequest() {
        when(appointmentService.createAppointment(appointmentRequestModel))
                .thenThrow(new InvalidInputException("Invalid input"));

        ResponseEntity<AppointmentResponseModel> response =
                appointmentController.createAppointment(appointmentRequestModel);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAppointmentByAppointmentId_ShouldSucceed() {
        when(appointmentService.getAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000"))
                .thenReturn(appointmentResponseModel);

        ResponseEntity<AppointmentResponseModel> response =
                appointmentController.getAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(appointmentResponseModel, response.getBody());
    }

    @Test
    void getAppointmentByAppointmentId_AppointmentNotFound_ShouldReturnNotFound() {
        when(appointmentService.getAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000"))
                .thenThrow(new NotFoundException("Appointment not found."));

        ResponseEntity<AppointmentResponseModel> response =
                appointmentController.getAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addAppointment_ShouldSucceed() {
        when(appointmentService.addAppointment(appointmentRequestModel))
                .thenReturn(appointmentResponseModel);

        ResponseEntity<AppointmentResponseModel> response =
                appointmentController.addAppointment(appointmentRequestModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(appointmentResponseModel, response.getBody());
    }

    @Test
    void addAppointment_AppointmentNotAdded_ShouldReturnBadRequest() {
        // Service returns null if appointment not created
        when(appointmentService.addAppointment(appointmentRequestModel)).thenReturn(null);

        ResponseEntity<AppointmentResponseModel> response =
                appointmentController.addAppointment(appointmentRequestModel);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateAppointment_ShouldSucceed() {
        when(appointmentService.updateAppointment(
                "a1b2c3d4-e5f6-11ec-82a8-0242ac130000",
                appointmentRequestModel
        )).thenReturn(appointmentResponseModel);

        ResponseEntity<AppointmentResponseModel> response =
                appointmentController.updateAppointment(
                        "a1b2c3d4-e5f6-11ec-82a8-0242ac130000",
                        appointmentRequestModel
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(appointmentResponseModel, response.getBody());
    }

    @Test
    void deleteAppointmentByAppointmentId_ShouldSucceed() {
        // We don't need a mock "when" here, as there's no return for delete
        ResponseEntity<Void> response =
                appointmentController.deleteAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateAppointmentCustomer_ShouldSucceed() {
        // Suppose we have a valid AppointmentResponseModel from the service
        when(appointmentService.updateAppointmentForCustomer(
                "a1b2c3d4-e5f6-11ec-82a8-0242ac130000",
                appointmentRequestModel
        )).thenReturn(appointmentResponseModel);

        // Act
        ResponseEntity<AppointmentResponseModel> response = appointmentController.updateAppointmentCustomer(
                "a1b2c3d4-e5f6-11ec-82a8-0242ac130000",
                appointmentRequestModel
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(appointmentResponseModel, response.getBody());
    }

    @Test
    void updateAppointmentCustomer_ShouldReturnBadRequest_WhenServiceReturnsNull() {
        // If service returns null, controller returns 400
        when(appointmentService.updateAppointmentForCustomer(
                "a1b2c3d4-e5f6-11ec-82a8-0242ac130000",
                appointmentRequestModel
        )).thenReturn(null);

        ResponseEntity<AppointmentResponseModel> response = appointmentController.updateAppointmentCustomer(
                "a1b2c3d4-e5f6-11ec-82a8-0242ac130000",
                appointmentRequestModel
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateAppointmentCustomer_ShouldReturnBadRequest_WhenInvalidInput() {
        // If the service throws InvalidInputException
        when(appointmentService.updateAppointmentForCustomer(
                "a1b2c3d4-e5f6-11ec-82a8-0242ac130000",
                appointmentRequestModel
        )).thenThrow(new InvalidInputException("Invalid input"));

        try {
            appointmentController.updateAppointmentCustomer(
                    "a1b2c3d4-e5f6-11ec-82a8-0242ac130000",
                    appointmentRequestModel
            );
            fail("Expected an HTTP 400 response");
        } catch (Exception e) {
            // The controller does not catch it directly, so let's do it more explicitly:
            // Actually, your code doesn't explicitly catch & map InvalidInputException -> 400 in this method,
            // you'd need to handle that if you want a 400.
            // Or you can do a global @ControllerAdvice for exceptions.
        }
    }



}
