package com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentResponseMapper {
    @Mapping(expression = "java(appointment.getAppointmentIdentifier().getAppointmentId())", target = "appointmentId")
    @Mapping(target = "customerId", source = "appointment.customerId")
    @Mapping(target= "status", source = "appointment.status")
    AppointmentResponseModel entityToResponseModel(Appointment appointment);

    List<AppointmentResponseModel> entityToResponseModelList(List<Appointment> appointments);
}
