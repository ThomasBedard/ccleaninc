package com.ccleaninc.cclean.appointmentssubdomain.datalayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
public class AppointmentIdentifier {

    private String appointmentId;

    public AppointmentIdentifier(){
        this.appointmentId = java.util.UUID.randomUUID().toString();
    }

    public String getAppointmentId() {
        return appointmentId;
    }
}
