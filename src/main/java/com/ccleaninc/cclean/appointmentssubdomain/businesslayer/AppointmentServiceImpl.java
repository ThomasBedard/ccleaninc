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
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
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


    @Override
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

    @Override
    public List<AppointmentResponseModel> getAppointmentsByCustomerId(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new InvalidInputException("Customer ID cannot be null or empty.");
        }

        var optionalCustomer = customerRepository.findByCustomerIdentifier_CustomerId(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("No customer found for ID: " + customerId);
        }

        // Use the correct repository method name:
        var appointments = appointmentRepository.findAllByCustomerId(customerId);

        return appointmentResponseMapper.entityToResponseModelList(appointments);
    }

    @Override
    public AppointmentResponseModel updateAppointmentForCustomer(String appointmentId, AppointmentRequestModel requestModel) {
        // Similar validation
        if (appointmentId == null || appointmentId.length() != 36) {
            throw new InvalidInputException("Appointment ID must be 36 chars");
        }
        Appointment appointment = appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId);
        if (appointment == null) {
            throw new NotFoundException("Appointment not found for " + appointmentId);
        }

        // Keep the existing appointment.getCustomerId()
        // DO NOT overwrite it with random
        appointment.setCustomerFirstName(requestModel.getCustomerFirstName());
        appointment.setCustomerLastName(requestModel.getCustomerLastName());
        // appointment.setCustomerId(...)  <-- not touched
        appointment.setAppointmentDate(requestModel.getAppointmentDate());
        appointment.setServices(requestModel.getServices());
        appointment.setComments(requestModel.getComments());
        appointment.setStatus(requestModel.getStatus());

        Appointment saved = appointmentRepository.save(appointment);
        return appointmentResponseMapper.entityToResponseModel(saved);
    }

}
