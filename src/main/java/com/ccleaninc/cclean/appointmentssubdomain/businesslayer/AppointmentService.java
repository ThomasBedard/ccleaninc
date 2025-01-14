package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;

import java.util.List;

public interface AppointmentService {

    List<AppointmentResponseModel> getAllAppointments();


}
