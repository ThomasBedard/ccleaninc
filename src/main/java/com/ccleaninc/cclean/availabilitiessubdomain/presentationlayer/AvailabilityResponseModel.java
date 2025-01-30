package com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer;

import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Shift;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
public class AvailabilityResponseModel {
    
    private Integer id; // Primary key from the database
    private String availabilityId; // Unique identifier for this availability record
    private String employeeFirstName;
    private String employeeLastName;
    private String employeeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime availableDate; // Date and time the employee is available

    private Shift shift; // Enum for shift type (MORNING, EVENING, NIGHT)
    private String comments; // Additional information or notes
}
