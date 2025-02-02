package com.ccleaninc.cclean.employeeschedulesubdomain.datamapperlayer;

import com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.EmployeeSchedule;
import com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer.EmployeeScheduleRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeScheduleRequestMapper {

    @Mapping(target = "employeeScheduleIdentifier", ignore = true) // UUID will be generated internally
    @Mapping(target = "id", ignore = true) // Handled by the embedded identifier
    EmployeeSchedule requestModelToEntity(EmployeeScheduleRequestModel requestModel);
}
