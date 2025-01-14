package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;


import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.AppointmentRepository;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
import com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer.AppointmentResponseMapper;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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
        // Arrange
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));
        when(appointmentResponseMapper.entityToResponseModelList(List.of(appointment))).thenReturn(List.of(appointmentResponseModel));


        // Act
        List<AppointmentResponseModel> appointments = appointmentService.getAllAppointments();

        // Assert
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
        // Arrange
        when(appointmentRepository.findAll()).thenReturn(List.of());
        when(appointmentResponseMapper.entityToResponseModelList(List.of())).thenReturn(List.of());

        // Act
        List<AppointmentResponseModel> appointments = appointmentService.getAllAppointments();

        // Assert
        assertTrue(appointments.isEmpty());
    }


}
