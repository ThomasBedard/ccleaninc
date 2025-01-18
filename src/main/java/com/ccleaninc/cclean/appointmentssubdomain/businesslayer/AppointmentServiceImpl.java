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
<<<<<<< HEAD
=======
import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceIdentifier;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
=======
>>>>>>> 30f5822 (fix(CCICC-68): Fixed appointment creation to correctly use customer ID and updated unit tests)
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

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final AppointmentResponseMapper appointmentResponseMapper;
    private final AppointmentRequestMapper appointmentRequestMapper;


    @Override
    public List<AppointmentResponseModel> getAllAppointments() {
        return appointmentResponseMapper.entityToResponseModelList(appointmentRepository.findAll());
    }


    @Override
<<<<<<< HEAD
    public AppointmentResponseModel createAppointment(AppointmentRequestModel requestModel) {
        if (requestModel.getCustomerId() == null || requestModel.getCustomerId().isBlank()) {
            throw new InvalidInputException("Customer ID is required.");
        }
        if (requestModel.getAppointmentDate() == null) {
            throw new InvalidInputException("Appointment date/time is required.");
        }

        var optionalCustomer = customerRepository.findByCustomerIdentifier_CustomerId(requestModel.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("No customer found for ID: " + requestModel.getCustomerId());
        }
        var foundCustomer = optionalCustomer.get();

        Appointment newAppointment = Appointment.builder()
                .appointmentIdentifier(new AppointmentIdentifier()) // auto-generated UUID
                .customerId(foundCustomer.getCustomerIdentifier().getCustomerId())  // store the actual ID
                .customerFirstName(foundCustomer.getFirstName())
                .customerLastName(foundCustomer.getLastName())
                .appointmentDate(requestModel.getAppointmentDate())
                .services(requestModel.getServices())   // e.g. "id1,id2"
                .comments(requestModel.getComments())
                .status(requestModel.getStatus() == null ? Status.pending : requestModel.getStatus())
                .build();

        Appointment savedAppointment = appointmentRepository.save(newAppointment);
        return appointmentResponseMapper.entityToResponseModel(savedAppointment);
    }

=======
    public AppointmentResponseModel getAppointmentByAppointmentId(String appointmentId) {
        if (appointmentId == null || appointmentId.length() != 36) {
            throw new InvalidInputException("Appointment ID must be a valid 36-character string: " + appointmentId);
        }
        Appointment appointment = appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId);
        if (appointment == null) {
            throw new NotFoundException("Appointment with ID " + appointmentId + " was not found.");
        }
        return appointmentResponseMapper.entityToResponseModel(appointment);
    }


    @Override
    public AppointmentResponseModel addAppointment(AppointmentRequestModel appointmentRequestModel) {
        if (appointmentRequestModel == null) {
            throw new InvalidInputException("Appointment request model cannot be null.");
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentIdentifier(new AppointmentIdentifier()); // UUID

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
        if (appointmentId == null || appointmentId.length() != 36) {
            throw new InvalidInputException("Appointment ID must be a valid 36-character string: " + appointmentId);
        }
        if (appointmentRequestModel == null) {
            throw new InvalidInputException("Appointment request model cannot be null.");
        }

        Appointment appointment = appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId);
        if (appointment == null) {
            throw new NotFoundException("Appointment with ID " + appointmentId + " was not found.");
        }

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
            throw new InvalidInputException("Appointment ID must be a valid 36-character string: " + appointmentId);
        }
        Appointment existing = appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId);
        if (existing == null) {
            throw new NotFoundException("Appointment with ID " + appointmentId + " was not found.");
        }
        appointmentRepository.delete(existing);
    }

<<<<<<< HEAD
>>>>>>> 93ff3db (Implemented the CRUD operations for Appointments)

=======
>>>>>>> 30f5822 (fix(CCICC-68): Fixed appointment creation to correctly use customer ID and updated unit tests)
}
