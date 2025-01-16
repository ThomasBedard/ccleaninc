package com.ccleaninc.cclean.appointmentssubdomain.presentationlayer;

import com.ccleaninc.cclean.appointmentssubdomain.businesslayer.AppointmentService;
import com.ccleaninc.cclean.customerssubdomain.businesslayer.CustomerService;
import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerRepository;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AppointmentController {

    final private AppointmentService appointmentService;
    final private CustomerRepository customerRepository;

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseModel>> getAllAppointments() {
        List<AppointmentResponseModel> appointments = appointmentService.getAllAppointments();
        if (appointments == null || appointments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponseModel> createAppointment(@RequestBody AppointmentRequestModel requestModel) {
        try {
            AppointmentResponseModel createdAppointment = appointmentService.createAppointment(requestModel);
            return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
