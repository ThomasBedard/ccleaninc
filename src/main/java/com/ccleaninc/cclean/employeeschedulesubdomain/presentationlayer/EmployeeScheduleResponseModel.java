package com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer;

import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Shift;
import com.ccleaninc.cclean.employeeschedulesubdomain.datalayer.ScheduleStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
public class EmployeeScheduleResponseModel {

    private String scheduleId;          
    private String employeeId;          
    private String availabilityId;      

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime assignedDate; 

    private Shift shift;                
    private ScheduleStatus status;     
    private String comments;            
}
