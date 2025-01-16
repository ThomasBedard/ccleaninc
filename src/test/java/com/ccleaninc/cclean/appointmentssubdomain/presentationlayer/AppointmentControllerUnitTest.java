package com.ccleaninc.cclean.appointmentssubdomain.presentationlayer;

import com.ccleaninc.cclean.appointmentssubdomain.businesslayer.AppointmentService;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> a2bb356 (Added the code for the implementation)
import com.ccleaninc.cclean.servicesubdomain.businesslayer.ServiceService;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceController;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
=======
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)
=======
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
>>>>>>> 30f5822 (fix(CCICC-68): Fixed appointment creation to correctly use customer ID and updated unit tests)
=======
>>>>>>> a2bb356 (Added the code for the implementation)
=======
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
>>>>>>> 5876d42 (feat(CCICC-15): Implement multiple service selection flow)
=======
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)
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

<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.*;
=======
import static org.junit.jupiter.api.Assertions.assertEquals;
>>>>>>> a2bb356 (Added the code for the implementation)
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentControllerUnitTest {
<<<<<<< HEAD

=======
>>>>>>> a2bb356 (Added the code for the implementation)
    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;
<<<<<<< HEAD

=======
>>>>>>> a2bb356 (Added the code for the implementation)
    private AppointmentResponseModel appointmentResponseModel;
    private AppointmentRequestModel appointmentRequestModel;

    @BeforeEach
    void setUp() {
        LocalDateTime appointmentDate = LocalDateTime.parse("2021-08-01T10:00");
<<<<<<< HEAD

        // A typical response model you expect back
        appointmentResponseModel = AppointmentResponseModel.builder()
                .id(1)
                .appointmentId("123e4567-e89b-12d3-a456-426614174000")
                .customerId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")
=======
        appointmentResponseModel = AppointmentResponseModel.builder()
                .id(1)
<<<<<<< HEAD
                .customerId("1")
>>>>>>> a2bb356 (Added the code for the implementation)
=======
                .customerId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)
                .appointmentDate(appointmentDate)
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();
<<<<<<< HEAD

        // A typical request model you send in
        appointmentRequestModel = AppointmentRequestModel.builder()
=======
        appointmentRequestModel = AppointmentRequestModel.builder()
<<<<<<< HEAD
                .customerId("1")
>>>>>>> a2bb356 (Added the code for the implementation)
=======
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)
                .appointmentDate(appointmentDate)
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();
    }

    @Test
    void getAllAppointments_ShouldSucceed() {
        when(appointmentService.getAllAppointments()).thenReturn(List.of(appointmentResponseModel));

<<<<<<< HEAD
        ResponseEntity<List<AppointmentResponseModel>> response =
                appointmentController.getAllAppointments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
=======
        ResponseEntity<List<AppointmentResponseModel>> response = appointmentController.getAllAppointments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
>>>>>>> a2bb356 (Added the code for the implementation)
        assertEquals(1, response.getBody().size());
        assertEquals(appointmentResponseModel, response.getBody().get(0));
    }

    @Test
    void getAllAppointments_NoAppointmentsFound_ShouldReturnNotFound() {
        when(appointmentService.getAllAppointments()).thenReturn(List.of());

<<<<<<< HEAD
        ResponseEntity<List<AppointmentResponseModel>> response =
                appointmentController.getAllAppointments();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
<<<<<<< HEAD
    void createAppointment_ShouldSucceed() {
        when(appointmentService.createAppointment(appointmentRequestModel))
                .thenReturn(appointmentResponseModel);

<<<<<<< HEAD
        ResponseEntity<AppointmentResponseModel> response = appointmentController.createAppointment(appointmentRequestModel);
=======
=======
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
>>>>>>> 30f5822 (fix(CCICC-68): Fixed appointment creation to correctly use customer ID and updated unit tests)
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

<<<<<<< HEAD
        ResponseEntity<AppointmentResponseModel> response = appointmentController.addAppointment(appointmentRequestModel);
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)
=======
        ResponseEntity<AppointmentResponseModel> response =
                appointmentController.addAppointment(appointmentRequestModel);
>>>>>>> 30f5822 (fix(CCICC-68): Fixed appointment creation to correctly use customer ID and updated unit tests)

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(appointmentResponseModel, response.getBody());
    }

    @Test
<<<<<<< HEAD
<<<<<<< HEAD
    void createAppointment_InvalidInput_ShouldReturnBadRequest() {
        when(appointmentService.createAppointment(appointmentRequestModel))
                .thenThrow(new InvalidInputException("Invalid input"));

        ResponseEntity<AppointmentResponseModel> response = appointmentController.createAppointment(appointmentRequestModel);
=======
=======
>>>>>>> 30f5822 (fix(CCICC-68): Fixed appointment creation to correctly use customer ID and updated unit tests)
    void addAppointment_AppointmentNotAdded_ShouldReturnBadRequest() {
        // Service returns null if appointment not created
        when(appointmentService.addAppointment(appointmentRequestModel)).thenReturn(null);

<<<<<<< HEAD
        ResponseEntity<AppointmentResponseModel> response = appointmentController.addAppointment(appointmentRequestModel);
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)
=======
        ResponseEntity<AppointmentResponseModel> response =
                appointmentController.addAppointment(appointmentRequestModel);
>>>>>>> 30f5822 (fix(CCICC-68): Fixed appointment creation to correctly use customer ID and updated unit tests)

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> 30f5822 (fix(CCICC-68): Fixed appointment creation to correctly use customer ID and updated unit tests)
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

<<<<<<< HEAD
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)
=======
>>>>>>> 30f5822 (fix(CCICC-68): Fixed appointment creation to correctly use customer ID and updated unit tests)
=======
        ResponseEntity<List<AppointmentResponseModel>> response = appointmentController.getAllAppointments();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> a2bb356 (Added the code for the implementation)
=======

    @Test
    void createAppointment_ShouldSucceed() {
        when(appointmentService.createAppointment(appointmentRequestModel)).thenReturn(appointmentResponseModel);

        ResponseEntity<AppointmentResponseModel> response = appointmentController.createAppointment(appointmentRequestModel);
=======

    @Test
    void getAppointmentByAppointmentId_ShouldSucceed() {
        when(appointmentService.getAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")).thenReturn(appointmentResponseModel);

        ResponseEntity<AppointmentResponseModel> response = appointmentController.getAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointmentResponseModel, response.getBody());
    }

    @Test
    void getAppointmentByAppointmentId_AppointmentNotFound_ShouldReturnNotFound() {
        when(appointmentService.getAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")).thenThrow(new NotFoundException("Appointment with ID a1b2c3d4-e5f6-11ec-82a8-0242ac130000 was not found."));

        ResponseEntity<AppointmentResponseModel> response = appointmentController.getAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addAppointment_ShouldSucceed() {
        when(appointmentService.addAppointment(appointmentRequestModel)).thenReturn(appointmentResponseModel);

        ResponseEntity<AppointmentResponseModel> response = appointmentController.addAppointment(appointmentRequestModel);
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(appointmentResponseModel, response.getBody());
    }

    @Test
<<<<<<< HEAD
    void createAppointment_InvalidInput_ShouldReturnBadRequest() {
        when(appointmentService.createAppointment(appointmentRequestModel))
                .thenThrow(new InvalidInputException("Invalid input"));

        ResponseEntity<AppointmentResponseModel> response = appointmentController.createAppointment(appointmentRequestModel);
=======
    void addAppointment_AppointmentNotAdded_ShouldReturnBadRequest() {
        when(appointmentService.addAppointment(appointmentRequestModel)).thenReturn(null);

        ResponseEntity<AppointmentResponseModel> response = appointmentController.addAppointment(appointmentRequestModel);
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

<<<<<<< HEAD
>>>>>>> 5876d42 (feat(CCICC-15): Implement multiple service selection flow)
=======
    @Test
    void updateAppointment_ShouldSucceed() {
        when(appointmentService.updateAppointment("a1b2c3d4-e5f6-11ec-82a8-0242ac130000", appointmentRequestModel)).thenReturn(appointmentResponseModel);

        ResponseEntity<AppointmentResponseModel> response = appointmentController.updateAppointment("a1b2c3d4-e5f6-11ec-82a8-0242ac130000", appointmentRequestModel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointmentResponseModel, response.getBody());
    }

    @Test
    void deleteAppointmentByAppointmentId_ShouldSucceed() {
        ResponseEntity<Void> response = appointmentController.deleteAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)
}
