package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentRequestModel;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;

import java.util.List;

public interface AppointmentService {

    List<AppointmentResponseModel> getAllAppointments();
<<<<<<< HEAD
    AppointmentResponseModel createAppointment(AppointmentRequestModel requestModel);
<<<<<<< HEAD
}
=======
=======

>>>>>>> 30f5822 (fix(CCICC-68): Fixed appointment creation to correctly use customer ID and updated unit tests)
    AppointmentResponseModel getAppointmentByAppointmentId(String appointmentId);

    AppointmentResponseModel addAppointment(AppointmentRequestModel appointmentRequestModel);

    AppointmentResponseModel updateAppointment(String appointmentId, AppointmentRequestModel appointmentRequestModel);

    void deleteAppointmentByAppointmentId(String appointmentId);

}
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)
