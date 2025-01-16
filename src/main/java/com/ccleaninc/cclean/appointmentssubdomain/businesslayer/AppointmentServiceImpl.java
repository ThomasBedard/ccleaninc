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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
