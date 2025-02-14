package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentRequestModel;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.util.List;

public interface AppointmentService {

    List<AppointmentResponseModel> getAllAppointments();

    AppointmentResponseModel createAppointment(AppointmentRequestModel requestModel);

    AppointmentResponseModel getAppointmentByAppointmentId(String appointmentId);

    AppointmentResponseModel addAppointment(AppointmentRequestModel appointmentRequestModel);

    AppointmentResponseModel updateAppointment(String appointmentId, AppointmentRequestModel appointmentRequestModel);

    void deleteAppointmentByAppointmentId(String appointmentId);
    List<AppointmentResponseModel> getAppointmentsByCustomerId(String customerId);

    AppointmentResponseModel updateAppointmentForCustomer(String appointmentId, AppointmentRequestModel requestModel); // Customer


    ByteArrayOutputStream generateAppointmentsPdf();

    AppointmentResponseModel addAppointmentToCustomerAccount(String customerId, AppointmentRequestModel appointmentRequestModel) throws MessagingException;

    Page<AppointmentResponseModel> getAllAppointmentsPagination (Pageable pageable);

    List<AppointmentResponseModel> getAppointmentsByCustomerEmail(String email);
}
