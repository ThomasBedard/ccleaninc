package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.AppointmentIdentifier;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.AppointmentRepository;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
import com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer.AppointmentRequestMapper;
import com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer.AppointmentResponseMapper;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentRequestModel;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;
import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerRepository;
<<<<<<< HEAD
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
=======
import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceIdentifier;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private AppointmentRepository appointmentRepository;
    private CustomerRepository customerRepository;
    private AppointmentResponseMapper appointmentResponseMapper;
    private AppointmentRequestMapper appointmentRequestMapper;

    @Override
    public List<AppointmentResponseModel> getAllAppointments() {
        return appointmentResponseMapper.entityToResponseModelList(appointmentRepository.findAll());


    }

    @Override
<<<<<<< HEAD
    public AppointmentResponseModel createAppointment(AppointmentRequestModel requestModel) {
        // Validate
        if (requestModel.getCustomerId() == null || requestModel.getCustomerId().isBlank()) {
            throw new InvalidInputException("Customer ID is required.");
        }
        if (requestModel.getAppointmentDate() == null) {
            throw new InvalidInputException("Appointment date/time is required.");
        }
        // If needed, you can also validate requestModel.getServices() is not empty, etc.

        // Build the new Appointment entity
        Appointment newAppointment = Appointment.builder()
                .appointmentIdentifier(new AppointmentIdentifier())   // auto-generated UUID
                .customerId(requestModel.getCustomerId())
                .appointmentDate(requestModel.getAppointmentDate())
                .services(requestModel.getServices())  // e.g. "id1,id2,id3"
                .comments(requestModel.getComments())
                .status(requestModel.getStatus() == null ? Status.pending : requestModel.getStatus())
                .build();

        Appointment savedAppointment = appointmentRepository.save(newAppointment);
        return appointmentResponseMapper.entityToResponseModel(savedAppointment);
    }

=======
    public AppointmentResponseModel getAppointmentByAppointmentId(String appointmentId) {
        if (appointmentId == null || appointmentId.length() != 36) {
            throw new InvalidInputException("Appointment ID must be a valid 36-character string." + appointmentId);
        }
        Appointment appointment = appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId);
        if (appointment == null) {
            throw new NotFoundException("Appointment with ID " + appointmentId + " was not found.");
        }
        return appointmentResponseMapper.entityToResponseModel(appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId));
    }

    @Override
    public AppointmentResponseModel addAppointment(AppointmentRequestModel appointmentRequestModel) {
        if (appointmentRequestModel == null) {
            throw new InvalidInputException("Appointment request model cannot be null.");
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentIdentifier(new AppointmentIdentifier());
        appointment.setCustomerFirstName(appointmentRequestModel.getCustomerFirstName());
        appointment.setCustomerLastName(appointmentRequestModel.getCustomerLastName());
        appointment.setCustomerId(UUID.randomUUID().toString());
        appointment.setAppointmentDate(appointmentRequestModel.getAppointmentDate());
        appointment.setServices(appointmentRequestModel.getServices());
        appointment.setComments(appointmentRequestModel.getComments());
        appointment.setStatus(appointmentRequestModel.getStatus());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentResponseMapper.entityToResponseModel(savedAppointment);

    }

    @Override
    public AppointmentResponseModel updateAppointment(String appointmentId, AppointmentRequestModel appointmentRequestModel) {
        if (appointmentId.length() != 36) {
            throw new InvalidInputException("Appointment ID must be a valid 36-character string." + appointmentId);
        }
        if (appointmentId == null) {
            throw new NotFoundException("Appointment with ID " + appointmentId + " was not found.");
        }
        // Validate appointmentRequestModel
        if (appointmentRequestModel == null) {
            throw new InvalidInputException("Appointment request model cannot be null.");
        }


        Appointment appointment = appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId);
        appointment.setCustomerFirstName(appointmentRequestModel.getCustomerFirstName());
        appointment.setCustomerLastName(appointmentRequestModel.getCustomerLastName());
        appointment.setCustomerId(UUID.randomUUID().toString());
        appointment.setAppointmentDate(appointmentRequestModel.getAppointmentDate());
        appointment.setServices(appointmentRequestModel.getServices());
        appointment.setComments(appointmentRequestModel.getComments());
        appointment.setStatus(appointmentRequestModel.getStatus());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentResponseMapper.entityToResponseModel(savedAppointment);
    }

    @Override
    public void deleteAppointmentByAppointmentId(String appointmentId) {
        if (appointmentId == null || appointmentId.length() != 36) {
            throw new InvalidInputException("Appointment ID must be a valid 36-character string." + appointmentId);
        }
        appointmentRepository.delete(appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId));
    }

>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)

}
