package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.AppointmentRepository;
import com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer.AppointmentRequestMapper;
import com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer.AppointmentResponseMapper;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;
import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerRepository;
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

}
