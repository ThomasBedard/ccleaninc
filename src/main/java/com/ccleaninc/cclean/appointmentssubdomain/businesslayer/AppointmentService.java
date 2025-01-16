package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentRequestModel;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;

import java.util.List;

public interface AppointmentService {

    List<AppointmentResponseModel> getAllAppointments();
<<<<<<< HEAD
    AppointmentResponseModel createAppointment(AppointmentRequestModel requestModel);
}
=======
    AppointmentResponseModel getAppointmentByAppointmentId(String appointmentId);
    AppointmentResponseModel addAppointment(AppointmentRequestModel appointmentRequestModel);
    // update appointment
    AppointmentResponseModel updateAppointment (String appointmentId, AppointmentRequestModel appointmentRequestModel);

    //deleteAppointment
    void deleteAppointmentByAppointmentId(String appointmentId);

}
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)
