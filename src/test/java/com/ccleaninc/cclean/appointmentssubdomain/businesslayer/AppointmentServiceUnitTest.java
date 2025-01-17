package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.AppointmentRepository;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
import com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer.AppointmentResponseMapper;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentRequestModel;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;

import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;

import com.ccleaninc.cclean.servicesubdomain.datalayer.Service;
import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceIdentifier;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
                .customerId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")
                .appointmentDate(appointmentDate)
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();

        appointmentResponseModel = AppointmentResponseModel.builder()
                .id(1)
                .customerId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")
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
    void GetAppointmentByAppointmentId_shouldSucceed() {
        // Arrange
        when(appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")).thenReturn(appointment);
        when(appointmentResponseMapper.entityToResponseModel(appointment)).thenReturn(appointmentResponseModel);

        // Act
        AppointmentResponseModel appointment = appointmentService.getAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");

        // Assert
        assertEquals(appointmentResponseModel.getId(), appointment.getId());
        assertEquals(appointmentResponseModel.getCustomerId(), appointment.getCustomerId());
        assertEquals(appointmentResponseModel.getAppointmentDate(), appointment.getAppointmentDate());
        assertEquals(appointmentResponseModel.getServices(), appointment.getServices());
        assertEquals(appointmentResponseModel.getComments(), appointment.getComments());
        assertEquals(appointmentResponseModel.getStatus(), appointment.getStatus());
    }

    @Test
    void GetAppointmentByAppointmentId_shouldThrowInvalidInputException() {
        // Arrange
        when(appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")).thenReturn(null);

        // Act & Assert
        try {
            appointmentService.getAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");
        } catch (NotFoundException e) {
            assertEquals("Appointment with ID a1b2c3d4-e5f6-11ec-82a8-0242ac130000 was not found.", e.getMessage());
        }
    }

    @Test
    void deleteAppointmentByAppointmentId_shouldSucceed() {
        // Arrange
        when(appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")).thenReturn(appointment);

        // Act
        appointmentService.deleteAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");

    }

    @Test
    void deleteAppointmentByAppointmentId_shouldThrowNotFoundException() {
        // Arrange
        when(appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")).thenReturn(null);

        // Act & Assert
        try {
            appointmentService.deleteAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");
        } catch (NotFoundException e) {
            assertEquals("Appointment with ID a1b2c3d4-e5f6-11ec-82a8-0242ac130000 was not found.", e.getMessage());
        }
    }

    @Test
    void addAppointment_shouldSucceed() {
        // Arrange
        AppointmentRequestModel appointmentRequestModel = AppointmentRequestModel.builder()
                .customerFirstName("John")
                .customerLastName("Doe")
                .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();

        Appointment expectedAppointment = Appointment.builder()
                .customerFirstName("John")
                .customerLastName("Doe")
                .customerId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")
                .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(expectedAppointment);

        when(appointmentResponseMapper.entityToResponseModel(expectedAppointment)).thenReturn(appointmentResponseModel);

        // Act
        AppointmentResponseModel response = appointmentService.addAppointment(appointmentRequestModel);

        // Assert
        assertEquals(appointmentResponseModel.getId(), response.getId());
        assertEquals(appointmentResponseModel.getCustomerId(), response.getCustomerId());
        assertEquals(appointmentResponseModel.getAppointmentDate(), response.getAppointmentDate());
        assertEquals(appointmentResponseModel.getServices(), response.getServices());
        assertEquals(appointmentResponseModel.getComments(), response.getComments());
        assertEquals(appointmentResponseModel.getStatus(), response.getStatus());
    }

    @Test
    void addAppointment_shouldThrowInvalidInputException() {
        // Arrange
        AppointmentRequestModel appointmentRequestModel = null;

        // Act & Assert
        try {
            appointmentService.addAppointment(appointmentRequestModel);
        } catch (Exception e) {
            assertEquals("Appointment request model cannot be null.", e.getMessage());
        }
    }

    @Test
    void updateAppointment_shouldSucceed() {
        // Arrange
        AppointmentRequestModel appointmentRequestModel = AppointmentRequestModel.builder()
                .customerFirstName("John")
                .customerLastName("Doe")
                .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();

        Appointment expectedAppointment = Appointment.builder()
                .customerFirstName("John")
                .customerLastName("Doe")
                .customerId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")
                .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();

        when(appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000")).thenReturn(appointment);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(expectedAppointment);
        when(appointmentResponseMapper.entityToResponseModel(expectedAppointment)).thenReturn(appointmentResponseModel);

        // Act
        AppointmentResponseModel response = appointmentService.updateAppointment("a1b2c3d4-e5f6-11ec-82a8-0242ac130000", appointmentRequestModel);

        // Assert
        assertEquals(appointmentResponseModel.getId(), response.getId());
        assertEquals(appointmentResponseModel.getCustomerId(), response.getCustomerId());
        assertEquals(appointmentResponseModel.getAppointmentDate(), response.getAppointmentDate());
        assertEquals(appointmentResponseModel.getServices(), response.getServices());
        assertEquals(appointmentResponseModel.getComments(), response.getComments());
        assertEquals(appointmentResponseModel.getStatus(), response.getStatus());
    }

    @Test
    void updateAppointment_shouldThrowInvalidInputException() {
        // Arrange
        AppointmentRequestModel appointmentRequestModel = null;

        // Act & Assert
        try {
            appointmentService.updateAppointment("a1b2c3d4-e5f6-11ec-82a8-0242ac130000", appointmentRequestModel);
        } catch (Exception e) {
            assertEquals("Appointment request model cannot be null.", e.getMessage());
        }
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
