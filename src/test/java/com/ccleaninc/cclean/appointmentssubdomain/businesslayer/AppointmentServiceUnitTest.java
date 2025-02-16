package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.AppointmentRepository;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
import com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer.AppointmentResponseMapper;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentRequestModel;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;

import com.ccleaninc.cclean.customerssubdomain.datalayer.Customer;
import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerRepository;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;

import com.ccleaninc.cclean.utils.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceUnitTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentResponseMapper appointmentResponseMapper;

    @Mock
    private CustomerRepository customerRepository;

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
        when(appointmentResponseMapper.entityToResponseModelList(List.of(appointment)))
                .thenReturn(List.of(appointmentResponseModel));

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
        when(appointmentRepository
                .findAppointmentByAppointmentIdentifier_AppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000"))
                .thenReturn(appointment);
        when(appointmentResponseMapper.entityToResponseModel(appointment)).thenReturn(appointmentResponseModel);

        // Act
        AppointmentResponseModel appointment = appointmentService
                .getAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");

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
        when(appointmentRepository
                .findAppointmentByAppointmentIdentifier_AppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000"))
                .thenReturn(null);

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
        when(appointmentRepository
                .findAppointmentByAppointmentIdentifier_AppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000"))
                .thenReturn(appointment);

        // Act
        appointmentService.deleteAppointmentByAppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000");

    }

    @Test
    void deleteAppointmentByAppointmentId_shouldThrowNotFoundException() {
        // Arrange
        when(appointmentRepository
                .findAppointmentByAppointmentIdentifier_AppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000"))
                .thenReturn(null);

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

        when(appointmentRepository
                .findAppointmentByAppointmentIdentifier_AppointmentId("a1b2c3d4-e5f6-11ec-82a8-0242ac130000"))
                .thenReturn(appointment);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(expectedAppointment);
        when(appointmentResponseMapper.entityToResponseModel(expectedAppointment)).thenReturn(appointmentResponseModel);

        // Act
        AppointmentResponseModel response = appointmentService.updateAppointment("a1b2c3d4-e5f6-11ec-82a8-0242ac130000",
                appointmentRequestModel);

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

    /*
     * @Test
     * void CreateAppointment_shouldSucceed() {
     * AppointmentRequestModel requestModel = AppointmentRequestModel.builder()
     * .customerId("1")
     * .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
     * .services("services")
     * .comments("comments")
     * .status(Status.pending)
     * .build();
     * 
     * when(appointmentRepository.save(any(Appointment.class))).thenReturn(
     * appointment);
     * when(appointmentResponseMapper.entityToResponseModel(any(Appointment.class)))
     * .thenReturn(appointmentResponseModel);
     * 
     * AppointmentResponseModel response =
     * appointmentService.createAppointment(requestModel);
     * 
     * assertNotNull(response);
     * assertEquals(appointmentResponseModel.getId(), response.getId());
     * assertEquals(appointmentResponseModel.getCustomerId(),
     * response.getCustomerId());
     * }
     * 
     * @Test
     * void
     * CreateAppointment_shouldThrowInvalidInputException_whenCustomerIdIsNull() {
     * AppointmentRequestModel requestModel = AppointmentRequestModel.builder()
     * .customerId(null)
     * .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
     * .services("services")
     * .comments("comments")
     * .status(Status.pending)
     * .build();
     * 
     * InvalidInputException exception = assertThrows(InvalidInputException.class,
     * () -> {
     * appointmentService.createAppointment(requestModel);
     * });
     * 
     * assertEquals("Customer ID is required.", exception.getMessage());
     * }
     */

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

    @Test
    void getAppointmentsByCustomerId_ShouldSucceed() {
        // Setup
        when(customerRepository.findByCustomerIdentifier_CustomerId("c1d2e3f4"))
                .thenReturn(Optional.of(new Customer()));
        when(appointmentRepository.findAllByCustomerId("c1d2e3f4")).thenReturn(List.of(appointment));
        when(appointmentResponseMapper.entityToResponseModelList(List.of(appointment)))
                .thenReturn(List.of(appointmentResponseModel));

        // Act
        List<AppointmentResponseModel> result = appointmentService.getAppointmentsByCustomerId("c1d2e3f4");

        // Assert
        assertEquals(1, result.size());
        assertEquals(appointmentResponseModel, result.get(0));
        verify(customerRepository).findByCustomerIdentifier_CustomerId("c1d2e3f4");
        verify(appointmentRepository).findAllByCustomerId("c1d2e3f4");
    }

    @Test
    void getAppointmentsByCustomerId_ShouldThrowNotFound_WhenCustomerNotFound() {
        when(customerRepository.findByCustomerIdentifier_CustomerId("c1d2e3f4")).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> appointmentService.getAppointmentsByCustomerId("c1d2e3f4"));
        assertEquals("No customer found for ID: c1d2e3f4", ex.getMessage());
    }

    @Test
    void getAppointmentsByCustomerId_ShouldThrowInvalidInput_WhenCustomerIdIsBlank() {
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> appointmentService.getAppointmentsByCustomerId("   "));
        assertEquals("Customer ID cannot be null or empty.", ex.getMessage());
    }

    @Test
    void updateAppointmentForCustomer_ShouldThrowInvalidInput_WhenAppointmentIdIsInvalid() {
        // e.g. wrong length or null
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> appointmentService.updateAppointmentForCustomer(null, AppointmentRequestModel.builder().build()));
        assertEquals("Appointment ID must be a valid 36-character string.", ex.getMessage());
    }

    @Test
    void generateAppointmentPdf_InvalidInputException() {
        // Arrange
        when(appointmentRepository.findAll()).thenReturn(List.of());

        // Act & Assert
        try {
            appointmentService.generateAppointmentsPdf();
        } catch (Exception e) {
            assertEquals("No appointments found.", e.getMessage());
        }
    }

    @Test
    void generateAppointmentPdf_shouldSucceed() {
        // Arrange
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        // Act
        appointmentService.generateAppointmentsPdf();
    }

    @Test
    void generateAppointmentPdf_shouldReturnByteArrayOutputStream() {
        // Arrange
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        // Act
        appointmentService.generateAppointmentsPdf();
    }

    @Test
    void updateAppointment_shouldThrowInvalidInputExceptionWhenAppointmentIdIsInvalid() {
        // Arrange
        AppointmentRequestModel appointmentRequestModel = AppointmentRequestModel.builder()
                .customerFirstName("John")
                .customerLastName("Doe")
                .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
                .services("services")
                .comments("comments")
                .status(Status.pending)
                .build();

        // Act & Assert - Null appointmentId
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            appointmentService.updateAppointment(null, appointmentRequestModel);
        });
        assertEquals("Appointment ID must be a valid 36-character string: null", exception.getMessage());

        // Act & Assert - Invalid appointmentId (length not 36 characters)
        String invalidAppointmentId = "12345";
        Exception invalidIdException = assertThrows(InvalidInputException.class, () -> {
            appointmentService.updateAppointment(invalidAppointmentId, appointmentRequestModel);
        });
        assertEquals("Appointment ID must be a valid 36-character string: " + invalidAppointmentId,
                invalidIdException.getMessage());
    }

    @Test
    void getAppointmentById_shouldThrowInvalidInputExceptionWhenAppointmentIdIsInvalid() {
        // Arrange
        // Act & Assert - Null appointmentId
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            appointmentService.getAppointmentByAppointmentId(null);
        });
        assertEquals("Appointment ID must be a valid 36-character string: null", exception.getMessage());

        // Act & Assert - Invalid appointmentId (length not 36 characters)
        String invalidAppointmentId = "12345";
        Exception invalidIdException = assertThrows(InvalidInputException.class, () -> {
            appointmentService.getAppointmentByAppointmentId(invalidAppointmentId);
        });
        assertEquals("Appointment ID must be a valid 36-character string: " + invalidAppointmentId,
                invalidIdException.getMessage());
    }

    @Test
    void deleteAppointmentByAppointmentId_shouldThrowInvalidInputExceptionWhenAppointmentIdIsInvalid() {
        // Arrange
        // Act & Assert - Null appointmentId
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            appointmentService.deleteAppointmentByAppointmentId(null);
        });
        assertEquals("Appointment ID must be a valid 36-character string: null", exception.getMessage());

        // Act & Assert - Invalid appointmentId (length not 36 characters)
        String invalidAppointmentId = "12345";
        Exception invalidIdException = assertThrows(InvalidInputException.class, () -> {
            appointmentService.deleteAppointmentByAppointmentId(invalidAppointmentId);
        });
        assertEquals("Appointment ID must be a valid 36-character string: " + invalidAppointmentId,
                invalidIdException.getMessage());
    }

    @Test
    void getAppointmentsByCustomerId_ShouldSucceed2() {
        // Arrange
        String customerId = "c1d2e3f4";
        String jwtToken = "mock-jwt-token"; // Simulated JWT token

        when(customerRepository.findByCustomerIdentifier_CustomerId(customerId))
                .thenReturn(Optional.of(new Customer()));

        when(appointmentRepository.findAllByCustomerId(customerId))
                .thenReturn(List.of(appointment));

        when(appointmentResponseMapper.entityToResponseModelList(List.of(appointment)))
                .thenReturn(List.of(appointmentResponseModel));

        // Act
        List<AppointmentResponseModel> result = appointmentService.getAppointmentsByCustomerId(customerId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(appointmentResponseModel, result.get(0));
        verify(customerRepository).findByCustomerIdentifier_CustomerId(customerId);
        verify(appointmentRepository).findAllByCustomerId(customerId);
    }

    @Test
    void getAppointmentsByCustomerId_ShouldThrowNotFound_WhenCustomerNotFound2() {
        // Arrange
        String customerId = "c1d2e3f4";

        when(customerRepository.findByCustomerIdentifier_CustomerId(customerId))
                .thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> appointmentService.getAppointmentsByCustomerId(customerId));

        assertEquals("No customer found for ID: " + customerId, ex.getMessage());
    }

    @Test
    void getAppointmentsByCustomerId_ShouldThrowInvalidInput_WhenCustomerIdIsBlank2() {
        // Act & Assert
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> appointmentService.getAppointmentsByCustomerId("   "));

        assertEquals("Customer ID cannot be null or empty.", ex.getMessage());
    }

    @Test
    void getAllAppointmentsPagination_ShouldReturnPaginatedAppointments() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 7, Sort.by("id").ascending());
        List<Appointment> appointmentList = List.of(appointment);
        Page<Appointment> appointmentPage = new PageImpl<>(appointmentList, pageable, appointmentList.size());

        when(appointmentRepository.findAll(pageable)).thenReturn(appointmentPage);
        when(appointmentResponseMapper.entityToResponseModel(any(Appointment.class)))
                .thenReturn(appointmentResponseModel);

        // Act
        Page<AppointmentResponseModel> result = appointmentService.getAllAppointmentsPagination(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(appointmentResponseModel, result.getContent().get(0));
        verify(appointmentRepository).findAll(pageable);
        verify(appointmentResponseMapper).entityToResponseModel(any(Appointment.class));
    }

    @Test
    void getAllAppointmentsPagination_ShouldReturnEmptyPage_WhenNoAppointmentsExist() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 7);
        Page<Appointment> emptyPage = Page.empty();

        when(appointmentRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        Page<AppointmentResponseModel> result = appointmentService.getAllAppointmentsPagination(pageable);

        // Assert
        assertTrue(result.isEmpty());
        verify(appointmentRepository).findAll(pageable);
    }



}
