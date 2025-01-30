package com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer;

import java.time.LocalDateTime;

import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Shift;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AvailabilityRequestModel {
    
    private String employeeId;
    private String employeeFirstName;
    private String employeeLastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime availableDate;

    private Shift shift;  // Enum for the type of shift (MORNING, EVENING, NIGHT)

    private String comments;
}
