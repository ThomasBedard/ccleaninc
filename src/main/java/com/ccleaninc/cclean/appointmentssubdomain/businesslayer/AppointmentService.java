package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentRequestModel;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface AppointmentService {

    List<AppointmentResponseModel> getAllAppointments();
    AppointmentResponseModel getAppointmentByAppointmentId(String appointmentId);
    AppointmentResponseModel addAppointment(AppointmentRequestModel appointmentRequestModel);
    // update appointment
    AppointmentResponseModel updateAppointment (String appointmentId, AppointmentRequestModel appointmentRequestModel);
    void deleteAppointmentByAppointmentId(String appointmentId);
    ByteArrayOutputStream generateAppointmentsPdf();



}
