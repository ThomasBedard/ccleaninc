package com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.AppointmentIdentifier;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentResponseMapperTest {

    private AppointmentResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AppointmentResponseMapper.class);
    }

    @Test
    void entityToResponseModel_shouldMapCorrectly() {
        AppointmentIdentifier identifier1 = new AppointmentIdentifier();
        Appointment appointment = Appointment.builder()
                .appointmentIdentifier(identifier1)
                .customerId("12345")
                .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
                .services("Cleaning")
                .comments("Please be on time.")
                .status(Status.pending)
                .build();

        AppointmentResponseModel responseModel = mapper.entityToResponseModel(appointment);

        assertEquals(identifier1.getAppointmentId(), responseModel.getAppointmentId());
        assertEquals("12345", responseModel.getCustomerId());
        assertEquals(LocalDateTime.parse("2021-08-01T10:00"), responseModel.getAppointmentDate());
        assertEquals("Cleaning", responseModel.getServices());
        assertEquals("Please be on time.", responseModel.getComments());
        assertEquals(Status.pending, responseModel.getStatus());
    }

    @Test
    void entityToResponseModelList_shouldMapListCorrectly() {
        AppointmentIdentifier identifier1 = new AppointmentIdentifier();
        AppointmentIdentifier identifier2 = new AppointmentIdentifier();

        Appointment appointment1 = Appointment.builder()
                .appointmentIdentifier(identifier1)
                .customerId("12345")
                .appointmentDate(LocalDateTime.parse("2021-08-01T10:00"))
                .services("Cleaning")
                .comments("Please be on time.")
                .status(Status.pending)
                .build();

        Appointment appointment2 = Appointment.builder()
                .appointmentIdentifier(identifier2)
                .customerId("67890")
                .appointmentDate(LocalDateTime.parse("2021-09-01T14:00"))
                .services("Gardening")
                .comments("Ring the bell.")
                .status(Status.completed)
                .build();

        List<AppointmentResponseModel> responseModels = mapper.entityToResponseModelList(List.of(appointment1, appointment2));

        assertEquals(2, responseModels.size());
        assertEquals(identifier1.getAppointmentId(), responseModels.get(0).getAppointmentId());
        assertEquals(identifier2.getAppointmentId(), responseModels.get(1).getAppointmentId());
    }
}
