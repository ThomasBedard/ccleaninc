package com.ccleaninc.cclean.employeeschedulesubdomain.businesslayer;

import com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.EmployeeSchedule;
import com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.EmployeeScheduleIdentifier;
import com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.EmployeeScheduleRepository;
import com.ccleaninc.cclean.employeeschedulesubdomain.datamapperlayer.EmployeeScheduleRequestMapper;
import com.ccleaninc.cclean.employeeschedulesubdomain.datamapperlayer.EmployeeScheduleResponseMapper;
import com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer.EmployeeScheduleRequestModel;
import com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer.EmployeeScheduleResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeeScheduleServiceImpl implements EmployeeScheduleService {

    private final EmployeeScheduleRepository employeeScheduleRepository;
    private final EmployeeScheduleResponseMapper employeeScheduleResponseMapper;
    private final EmployeeScheduleRequestMapper employeeScheduleRequestMapper;

    @Override
    public List<EmployeeScheduleResponseModel> getAllSchedules() {
        return employeeScheduleResponseMapper.entityToResponseModelList(employeeScheduleRepository.findAll());
    }

    @Override
    public EmployeeScheduleResponseModel createSchedule(EmployeeScheduleRequestModel requestModel) {
        if (requestModel.getEmployeeId() == null || requestModel.getEmployeeId().isBlank()) {
            throw new InvalidInputException("Employee ID is required.");
        }
        if (requestModel.getAssignedDate() == null) {
            throw new InvalidInputException("Assigned date/time is required.");
        }

        EmployeeSchedule newSchedule = EmployeeSchedule.builder()
                .employeeScheduleIdentifier(new EmployeeScheduleIdentifier())
                .employeeId(requestModel.getEmployeeId())
                .availabilityId(requestModel.getAvailabilityId())
                .assignedDate(requestModel.getAssignedDate())
                .shift(requestModel.getShift())
                .status(requestModel.getStatus())
                .comments(requestModel.getComments())
                .build();

        EmployeeSchedule savedSchedule = employeeScheduleRepository.save(newSchedule);
        return employeeScheduleResponseMapper.entityToResponseModel(savedSchedule);
    }

    @Override
    public EmployeeScheduleResponseModel getScheduleByScheduleId(String scheduleId) {
        if (scheduleId == null || scheduleId.length() != 36) {
            throw new InvalidInputException("Schedule ID must be a valid 36-character string.");
        }
        EmployeeSchedule schedule = employeeScheduleRepository.findByEmployeeScheduleIdentifier_ScheduleId(scheduleId)
                .orElseThrow(() -> new NotFoundException("Schedule with ID " + scheduleId + " was not found."));
        return employeeScheduleResponseMapper.entityToResponseModel(schedule);
    }

    @Override
    public EmployeeScheduleResponseModel updateSchedule(String scheduleId, EmployeeScheduleRequestModel requestModel) {
        if (scheduleId == null || scheduleId.length() != 36) {
            throw new InvalidInputException("Schedule ID must be a valid 36-character string.");
        }
        if (requestModel == null) {
            throw new InvalidInputException("Schedule request model cannot be null.");
        }

        EmployeeSchedule schedule = employeeScheduleRepository.findByEmployeeScheduleIdentifier_ScheduleId(scheduleId)
                .orElseThrow(() -> new NotFoundException("Schedule with ID " + scheduleId + " was not found."));

        // Update fields
        schedule.setAssignedDate(requestModel.getAssignedDate());
        schedule.setShift(requestModel.getShift());
        schedule.setStatus(requestModel.getStatus());
        schedule.setComments(requestModel.getComments());

        EmployeeSchedule updatedSchedule = employeeScheduleRepository.save(schedule);
        return employeeScheduleResponseMapper.entityToResponseModel(updatedSchedule);
    }

    @Override
    public void deleteScheduleByScheduleId(String scheduleId) {
        if (scheduleId == null || scheduleId.length() != 36) {
            throw new InvalidInputException("Schedule ID must be a valid 36-character string.");
        }
        EmployeeSchedule schedule = employeeScheduleRepository.findByEmployeeScheduleIdentifier_ScheduleId(scheduleId)
                .orElseThrow(() -> new NotFoundException("Schedule with ID " + scheduleId + " was not found."));
        employeeScheduleRepository.delete(schedule);
    }

    @Override
    public List<EmployeeScheduleResponseModel> getSchedulesByEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isBlank()) {
            throw new InvalidInputException("Employee ID cannot be null or empty.");
        }
        List<EmployeeSchedule> schedules = employeeScheduleRepository.findByEmployeeId(employeeId);
        return employeeScheduleResponseMapper.entityToResponseModelList(schedules);
    }

    @Override
    public List<EmployeeScheduleResponseModel> searchSchedules(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            throw new InvalidInputException("Search term cannot be null or empty.");
        }
        List<EmployeeSchedule> schedules = employeeScheduleRepository.searchSchedulesAdvanced(searchTerm);
        return employeeScheduleResponseMapper.entityToResponseModelList(schedules);
    }
}
