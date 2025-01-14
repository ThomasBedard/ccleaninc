package com.ccleaninc.cclean.appointmentssubdomain.presentationlayer;

import com.ccleaninc.cclean.appointmentssubdomain.businesslayer.AppointmentService;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
public class AppointmentResponseModel  {

    private Integer id;
    private String appointmentId;
    private String customerId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime appointmentDate;
    private String services;
    private String comments;
    private Status status;


}
