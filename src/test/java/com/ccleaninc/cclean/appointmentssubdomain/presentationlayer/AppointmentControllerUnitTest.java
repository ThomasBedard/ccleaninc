package com.ccleaninc.cclean.appointmentssubdomain.presentationlayer;

import com.ccleaninc.cclean.appointmentssubdomain.businesslayer.AppointmentService;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
import com.ccleaninc.cclean.servicesubdomain.businesslayer.ServiceService;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceController;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        appointmentResponseModel = AppointmentResponseModel.builder()
                .id(1)
                .customerId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")
                .appointmentDate(appointmentDate)
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();
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

        ResponseEntity<List<AppointmentResponseModel>> response = appointmentController.getAllAppointments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(appointmentResponseModel, response.getBody().get(0));
    }

    @Test
    void getAllAppointments_NoAppointmentsFound_ShouldReturnNotFound() {
        when(appointmentService.getAllAppointments()).thenReturn(List.of());

        ResponseEntity<List<AppointmentResponseModel>> response = appointmentController.getAllAppointments();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

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

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(appointmentResponseModel, response.getBody());
    }

    @Test
    void addAppointment_AppointmentNotAdded_ShouldReturnBadRequest() {
        when(appointmentService.addAppointment(appointmentRequestModel)).thenReturn(null);

        ResponseEntity<AppointmentResponseModel> response = appointmentController.addAppointment(appointmentRequestModel);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

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

    @Test
    void generateAppointmentsPdf_ShouldSucceed() {
        when(appointmentService.generateAppointmentsPdf()).thenReturn(new ByteArrayOutputStream());

        ResponseEntity<byte[]> response = appointmentController.generateAppointmentsPdf();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
