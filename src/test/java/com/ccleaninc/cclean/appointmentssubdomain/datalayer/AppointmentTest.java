package com.ccleaninc.cclean.appointmentssubdomain.datalayer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTest {

    @Test
    void appointmentConstructor_ShouldInitializeFieldsCorrectly() {
        String customerId = "12345";
        LocalDateTime appointmentDate = LocalDateTime.parse("2021-08-01T10:00");
        Status status = Status.pending;
        String comments = "Test comment";

        Appointment appointment = new Appointment(customerId, appointmentDate, status, comments);

        assertNotNull(appointment.getAppointmentIdentifier());
        assertEquals(customerId, appointment.getCustomerId());
        assertEquals(appointmentDate, appointment.getAppointmentDate());
        assertEquals(status, appointment.getStatus());
        assertEquals(comments, appointment.getComments());
    }
}
