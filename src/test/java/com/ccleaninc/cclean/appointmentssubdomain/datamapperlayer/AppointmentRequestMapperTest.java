package com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentRequestMapperTest {

    private AppointmentRequestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AppointmentRequestMapper.class);
    }

    @Test
    void requestModelToEntity_shouldMapCorrectly() {
        AppointmentRequestModel requestModel = AppointmentRequestModel.builder()
                .customerId("12345")
                .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
                .services("Cleaning")
                .comments("Please be on time.")
                .status(com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status.pending)
                .build();

        Appointment appointment = mapper.requestModelToEntity(requestModel);

        assertNull(appointment.getId());
        assertNull(appointment.getAppointmentIdentifier());
        assertEquals("12345", appointment.getCustomerId());
        assertEquals(LocalDateTime.parse("2021-08-01T10:00"), appointment.getAppointmentDate());
        assertEquals("Cleaning", appointment.getServices());
        assertEquals("Please be on time.", appointment.getComments());
        assertEquals(com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status.pending, appointment.getStatus());
    }
}
