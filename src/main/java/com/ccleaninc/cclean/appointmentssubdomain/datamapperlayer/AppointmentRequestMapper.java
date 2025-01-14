package com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointmentIdentifier", ignore = true)
    Appointment requestModelToEntity(AppointmentRequestModel appointmentRequestModel);
}
