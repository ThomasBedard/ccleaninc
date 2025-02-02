package com.ccleaninc.cclean.employeeschedulesubdomain.businesslayer;

import com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.EmployeeSchedule;
import com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.EmployeeScheduleIdentifier;
import com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.EmployeeScheduleRepository;
import com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.ScheduleStatus;
import com.ccleaninc.cclean.employeeschedulesubdomain.datamapperlayer.EmployeeScheduleRequestMapper;
import com.ccleaninc.cclean.employeeschedulesubdomain.datamapperlayer.EmployeeScheduleResponseMapper;
import com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer.EmployeeScheduleRequestModel;
import com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer.EmployeeScheduleResponseModel;
import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Shift;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeScheduleServiceUnitTest {

    @Mock
    private EmployeeScheduleRepository employeeScheduleRepository;

    @Mock
    private EmployeeScheduleResponseMapper employeeScheduleResponseMapper;

    @Mock
    private EmployeeScheduleRequestMapper employeeScheduleRequestMapper;

    @InjectMocks
    private EmployeeScheduleServiceImpl employeeScheduleService;

    private EmployeeSchedule employeeSchedule;
    private EmployeeScheduleResponseModel employeeScheduleResponseModel;

    @BeforeEach
    void setUp() {
        employeeSchedule = EmployeeSchedule.builder()
                .employeeScheduleIdentifier(new EmployeeScheduleIdentifier("123e4567-e89b-12d3-a456-426614174000"))
                .employeeId("emp-001")
                .availabilityId("avail-001")
                .assignedDate(LocalDateTime.now())
                .shift(Shift.MORNING)
                .status(ScheduleStatus.APPROVED)
                .comments("Scheduled for morning shift")
                .build();

        employeeScheduleResponseModel = EmployeeScheduleResponseModel.builder()
                .scheduleId("123e4567-e89b-12d3-a456-426614174000")
                .employeeId("emp-001")
                .availabilityId("avail-001")
                .assignedDate(LocalDateTime.now())
                .shift(Shift.MORNING)
                .status(ScheduleStatus.APPROVED)
                .comments("Scheduled for morning shift")
                .build();
    }

    @Test
    void getAllSchedules_ShouldReturnSchedules() {
        when(employeeScheduleRepository.findAll()).thenReturn(List.of(employeeSchedule));
        when(employeeScheduleResponseMapper.entityToResponseModelList(List.of(employeeSchedule)))
                .thenReturn(List.of(employeeScheduleResponseModel));

        List<EmployeeScheduleResponseModel> response = employeeScheduleService.getAllSchedules();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(employeeScheduleResponseModel.getScheduleId(), response.get(0).getScheduleId());
    }

    @Test
    void getAllSchedules_NoSchedulesFound_ShouldReturnEmptyList() {
        when(employeeScheduleRepository.findAll()).thenReturn(Collections.emptyList());

        List<EmployeeScheduleResponseModel> response = employeeScheduleService.getAllSchedules();

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void getScheduleByScheduleId_ShouldReturnSchedule() {
        String scheduleId = "123e4567-e89b-12d3-a456-426614174000";
        when(employeeScheduleRepository.findByEmployeeScheduleIdentifier_ScheduleId(scheduleId))
                .thenReturn(Optional.of(employeeSchedule));
        when(employeeScheduleResponseMapper.entityToResponseModel(employeeSchedule))
                .thenReturn(employeeScheduleResponseModel);

        EmployeeScheduleResponseModel response = employeeScheduleService.getScheduleByScheduleId(scheduleId);

        assertNotNull(response);
        assertEquals(employeeScheduleResponseModel.getScheduleId(), response.getScheduleId());
    }

    @Test
    void getScheduleByScheduleId_NotFound_ShouldThrowNotFoundException() {
        // Use a valid UUID format
        String scheduleId = "123e4567-e89b-12d3-a456-426614174002";
        when(employeeScheduleRepository.findByEmployeeScheduleIdentifier_ScheduleId(scheduleId))
                .thenReturn(Optional.empty());
    
        // Expect NotFoundException since repository returns empty
        assertThrows(NotFoundException.class, () -> employeeScheduleService.getScheduleByScheduleId(scheduleId));
    }

    @Test
    void createSchedule_ShouldReturnCreatedSchedule() {
        EmployeeScheduleRequestModel requestModel = EmployeeScheduleRequestModel.builder()
                .employeeId("emp-001")
                .availabilityId("avail-001")
                .assignedDate(LocalDateTime.now())
                .shift(Shift.MORNING)
                .status(ScheduleStatus.APPROVED)
                .comments("Scheduled for morning shift")
                .build();

        when(employeeScheduleRepository.save(any(EmployeeSchedule.class))).thenReturn(employeeSchedule);
        when(employeeScheduleResponseMapper.entityToResponseModel(employeeSchedule))
                .thenReturn(employeeScheduleResponseModel);

        EmployeeScheduleResponseModel response = employeeScheduleService.createSchedule(requestModel);

        assertNotNull(response);
        assertEquals(employeeScheduleResponseModel.getScheduleId(), response.getScheduleId());
    }

    @Test
    void createSchedule_InvalidInput_ShouldThrowInvalidInputException() {
        EmployeeScheduleRequestModel requestModel = EmployeeScheduleRequestModel.builder()
                .employeeId(null)
                .build();

        assertThrows(InvalidInputException.class, () -> employeeScheduleService.createSchedule(requestModel));
    }

    @Test
    void deleteScheduleByScheduleId_ShouldSucceed() {
        String scheduleId = "123e4567-e89b-12d3-a456-426614174000";
        when(employeeScheduleRepository.findByEmployeeScheduleIdentifier_ScheduleId(scheduleId))
                .thenReturn(Optional.of(employeeSchedule));

        employeeScheduleService.deleteScheduleByScheduleId(scheduleId);

        // No exception should be thrown
    }

    @Test
    void deleteScheduleByScheduleId_NotFound_ShouldThrowNotFoundException() {
        // Use a valid UUID format
        String scheduleId = "123e4567-e89b-12d3-a456-426614174001";
        when(employeeScheduleRepository.findByEmployeeScheduleIdentifier_ScheduleId(scheduleId))
                .thenReturn(Optional.empty());
    
        // Expect NotFoundException since repository returns empty
        assertThrows(NotFoundException.class, () -> employeeScheduleService.deleteScheduleByScheduleId(scheduleId));
    }

    @Test
    void searchSchedules_ShouldReturnSchedules() {
        String searchTerm = "John";
        when(employeeScheduleRepository.searchSchedulesAdvanced(searchTerm)).thenReturn(List.of(employeeSchedule));
        when(employeeScheduleResponseMapper.entityToResponseModelList(List.of(employeeSchedule)))
                .thenReturn(List.of(employeeScheduleResponseModel));

        List<EmployeeScheduleResponseModel> response = employeeScheduleService.searchSchedules(searchTerm);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(employeeScheduleResponseModel.getScheduleId(), response.get(0).getScheduleId());
    }

    @Test
    void searchSchedules_NoSchedulesFound_ShouldReturnEmptyList() {
        String searchTerm = "Nonexistent";
        when(employeeScheduleRepository.searchSchedulesAdvanced(searchTerm)).thenReturn(Collections.emptyList());

        List<EmployeeScheduleResponseModel> response = employeeScheduleService.searchSchedules(searchTerm);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }
}
