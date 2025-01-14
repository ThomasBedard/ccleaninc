package com.ccleaninc.cclean.appointmentssubdomain.presentationlayer;

import com.ccleaninc.cclean.appointmentssubdomain.businesslayer.AppointmentService;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
import com.ccleaninc.cclean.servicesubdomain.businesslayer.ServiceService;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceController;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
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
                .customerId("1")
                .appointmentDate(appointmentDate)
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();
        appointmentRequestModel = AppointmentRequestModel.builder()
                .customerId("1")
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
}
