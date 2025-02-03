package com.ccleaninc.cclean.employeeschedulesubdomain.datamapperlayer;

import com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.EmployeeSchedule;
import com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer.EmployeeScheduleResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper to convert EmployeeSchedule entity to EmployeeScheduleResponseModel.
 */
@Mapper(componentModel = "spring")
public interface EmployeeScheduleResponseMapper {

    // Map individual EmployeeSchedule entity to EmployeeScheduleResponseModel
    @Mapping(expression = "java(employeeSchedule.getEmployeeScheduleIdentifier().getScheduleId())", target = "scheduleId")
    @Mapping(target = "employeeId", source = "employeeSchedule.employeeId")
    @Mapping(target = "availabilityId", source = "employeeSchedule.availabilityId")
    @Mapping(target = "assignedDate", source = "employeeSchedule.assignedDate")
    @Mapping(target = "shift", source = "employeeSchedule.shift")
    @Mapping(target = "status", source = "employeeSchedule.status")
    @Mapping(target = "comments", source = "employeeSchedule.comments")
    EmployeeScheduleResponseModel entityToResponseModel(EmployeeSchedule employeeSchedule);

    // Map a list of EmployeeSchedule entities to a list of EmployeeScheduleResponseModels
    List<EmployeeScheduleResponseModel> entityToResponseModelList(List<EmployeeSchedule> employeeSchedules);
}
