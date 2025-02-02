package com.ccleaninc.cclean.employeeschedulesubdomain.businesslayer;

import com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer.EmployeeScheduleRequestModel;
import com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer.EmployeeScheduleResponseModel;

import java.util.List;

public interface EmployeeScheduleService {

    // Retrieve all employee schedules
    List<EmployeeScheduleResponseModel> getAllSchedules();

    // Create a new employee schedule
    EmployeeScheduleResponseModel createSchedule(EmployeeScheduleRequestModel requestModel);

    // Retrieve a specific schedule by its unique identifier
    EmployeeScheduleResponseModel getScheduleByScheduleId(String scheduleId);

    // Update an existing schedule by its unique identifier
    EmployeeScheduleResponseModel updateSchedule(String scheduleId, EmployeeScheduleRequestModel requestModel);

    // Delete a schedule by its unique identifier
    void deleteScheduleByScheduleId(String scheduleId);

    // Retrieve all schedules for a specific employee
    List<EmployeeScheduleResponseModel> getSchedulesByEmployeeId(String employeeId);

    // Advanced Search: Search schedules by employee name, phone, email, or date
    List<EmployeeScheduleResponseModel> searchSchedules(String searchTerm);
}
