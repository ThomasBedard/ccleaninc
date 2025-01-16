package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.AppointmentRepository;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
import com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer.AppointmentResponseMapper;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentRequestModel;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
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
public class AppointmentServiceUnitTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentResponseMapper appointmentResponseMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Appointment appointment;

    private AppointmentResponseModel appointmentResponseModel;

    @BeforeEach
    void setUp() {
        LocalDateTime appointmentDate = LocalDateTime.parse("2021-08-01T10:00");
        appointment = Appointment.builder()
                .id(1)
                .customerId("1")
                .appointmentDate(appointmentDate)
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();

        appointmentResponseModel = AppointmentResponseModel.builder()
                .id(1)
                .customerId("1")
                .appointmentDate(appointmentDate)
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();
    }

    @Test
    void GetAllAppointments_shouldSucceed() {
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));
        when(appointmentResponseMapper.entityToResponseModelList(List.of(appointment))).thenReturn(List.of(appointmentResponseModel));

        List<AppointmentResponseModel> appointments = appointmentService.getAllAppointments();

        assertEquals(1, appointments.size());
        assertEquals(appointmentResponseModel.getId(), appointments.get(0).getId());
        assertEquals(appointmentResponseModel.getCustomerId(), appointments.get(0).getCustomerId());
        assertEquals(appointmentResponseModel.getAppointmentDate(), appointments.get(0).getAppointmentDate());
        assertEquals(appointmentResponseModel.getServices(), appointments.get(0).getServices());
        assertEquals(appointmentResponseModel.getComments(), appointments.get(0).getComments());
        assertEquals(appointmentResponseModel.getStatus(), appointments.get(0).getStatus());
    }

    @Test
    void GetAllAppointments_shouldReturnEmptyList() {
        when(appointmentRepository.findAll()).thenReturn(List.of());
        when(appointmentResponseMapper.entityToResponseModelList(List.of())).thenReturn(List.of());

        List<AppointmentResponseModel> appointments = appointmentService.getAllAppointments();

        assertTrue(appointments.isEmpty());
    }

    @Test
    void CreateAppointment_shouldSucceed() {
        AppointmentRequestModel requestModel = AppointmentRequestModel.builder()
                .customerId("1")
                .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentResponseMapper.entityToResponseModel(any(Appointment.class))).thenReturn(appointmentResponseModel);

        AppointmentResponseModel response = appointmentService.createAppointment(requestModel);

        assertNotNull(response);
        assertEquals(appointmentResponseModel.getId(), response.getId());
        assertEquals(appointmentResponseModel.getCustomerId(), response.getCustomerId());
    }

    @Test
    void CreateAppointment_shouldThrowInvalidInputException_whenCustomerIdIsNull() {
        AppointmentRequestModel requestModel = AppointmentRequestModel.builder()
                .customerId(null)
                .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            appointmentService.createAppointment(requestModel);
        });

        assertEquals("Customer ID is required.", exception.getMessage());
    }

    @Test
    void CreateAppointment_shouldThrowInvalidInputException_whenAppointmentDateIsNull() {
        AppointmentRequestModel requestModel = AppointmentRequestModel.builder()
                .customerId("1")
                .appointmentDate(null)
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            appointmentService.createAppointment(requestModel);
        });

        assertEquals("Appointment date/time is required.", exception.getMessage());
    }
}
