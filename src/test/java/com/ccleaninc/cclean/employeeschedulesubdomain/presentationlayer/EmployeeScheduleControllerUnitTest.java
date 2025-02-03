package com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer;

import com.ccleaninc.cclean.employeeschedulesubdomain.businesslayer.EmployeeScheduleService;
import com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer.EmployeeScheduleRequestModel;
import com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer.EmployeeScheduleResponseModel;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeScheduleControllerUnitTest {

    @Mock
    private EmployeeScheduleService employeeScheduleService;

    @InjectMocks
    private EmployeeScheduleController employeeScheduleController;

    private EmployeeScheduleResponseModel employeeScheduleResponseModel;
    private EmployeeScheduleRequestModel employeeScheduleRequestModel;

    @BeforeEach
    void setUp() {
        employeeScheduleResponseModel = EmployeeScheduleResponseModel.builder()
                .scheduleId("123e4567-e89b-12d3-a456-426614174000")
                .employeeId("emp-001")
                .availabilityId("avail-001")
                .assignedDate(LocalDateTime.now())
                .shift(com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Shift.MORNING)
                .status(com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.ScheduleStatus.APPROVED)
                .comments("Assigned for cleaning task")
                .build();

        employeeScheduleRequestModel = EmployeeScheduleRequestModel.builder()
                .employeeId("emp-001")
                .availabilityId("avail-001")
                .assignedDate(LocalDateTime.now())
                .shift(com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Shift.MORNING)
                .status(com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.ScheduleStatus.APPROVED)
                .comments("Assigned for cleaning task")
                .build();
    }

    @Test
    void getAllSchedules_ShouldReturnSchedules() {
        when(employeeScheduleService.getAllSchedules()).thenReturn(List.of(employeeScheduleResponseModel));

        ResponseEntity<List<EmployeeScheduleResponseModel>> response = employeeScheduleController.getAllSchedules();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(employeeScheduleResponseModel, response.getBody().get(0));
    }

    @Test
    void getAllSchedules_NoSchedulesFound_ShouldReturnNotFound() {
        when(employeeScheduleService.getAllSchedules()).thenReturn(Collections.emptyList());

        ResponseEntity<List<EmployeeScheduleResponseModel>> response = employeeScheduleController.getAllSchedules();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getScheduleByScheduleId_ShouldReturnSchedule() {
        when(employeeScheduleService.getScheduleByScheduleId("123e4567-e89b-12d3-a456-426614174000"))
                .thenReturn(employeeScheduleResponseModel);

        ResponseEntity<EmployeeScheduleResponseModel> response = employeeScheduleController.getScheduleByScheduleId("123e4567-e89b-12d3-a456-426614174000");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employeeScheduleResponseModel, response.getBody());
    }

    @Test
    void getScheduleByScheduleId_NotFound_ShouldReturnNotFound() {
        when(employeeScheduleService.getScheduleByScheduleId("123e4567-e89b-12d3-a456-426614174000"))
                .thenThrow(new NotFoundException("Schedule not found."));

        ResponseEntity<EmployeeScheduleResponseModel> response = employeeScheduleController.getScheduleByScheduleId("123e4567-e89b-12d3-a456-426614174000");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createSchedule_ShouldSucceed() {
        when(employeeScheduleService.createSchedule(employeeScheduleRequestModel)).thenReturn(employeeScheduleResponseModel);

        ResponseEntity<EmployeeScheduleResponseModel> response = employeeScheduleController.createSchedule(employeeScheduleRequestModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employeeScheduleResponseModel, response.getBody());
    }

    @Test
    void createSchedule_InvalidInput_ShouldReturnBadRequest() {
        when(employeeScheduleService.createSchedule(employeeScheduleRequestModel))
                .thenThrow(new InvalidInputException("Invalid input"));

        ResponseEntity<EmployeeScheduleResponseModel> response = employeeScheduleController.createSchedule(employeeScheduleRequestModel);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteScheduleByScheduleId_ShouldSucceed() {
        ResponseEntity<Void> response = employeeScheduleController.deleteSchedule("123e4567-e89b-12d3-a456-426614174000");


        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteScheduleByScheduleId_NotFound_ShouldReturnNotFound() {
        doThrow(new NotFoundException("Schedule not found."))
                .when(employeeScheduleService).deleteScheduleByScheduleId("123e4567-e89b-12d3-a456-426614174000");

                ResponseEntity<Void> response = employeeScheduleController.deleteSchedule("123e4567-e89b-12d3-a456-426614174000");


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
