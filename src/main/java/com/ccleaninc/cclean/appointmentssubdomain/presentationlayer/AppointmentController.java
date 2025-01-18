package com.ccleaninc.cclean.appointmentssubdomain.presentationlayer;

import com.ccleaninc.cclean.appointmentssubdomain.businesslayer.AppointmentService;
import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerRepository;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
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

    private final AppointmentService appointmentService;
    private final CustomerRepository customerRepository;

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseModel>> getAllAppointments() {
        List<AppointmentResponseModel> appointments = appointmentService.getAllAppointments();
        if (appointments == null || appointments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/appointments/with-customerid")
    public ResponseEntity<AppointmentResponseModel> createAppointment(@RequestBody AppointmentRequestModel requestModel) {
        // This method requires requestModel.getCustomerId() to exist
        try {
            AppointmentResponseModel createdAppointment = appointmentService.createAppointment(requestModel);
            return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentResponseModel> getAppointmentByAppointmentId(@PathVariable String appointmentId) {
        try {
            AppointmentResponseModel appointment = appointmentService.getAppointmentByAppointmentId(appointmentId);
            return ResponseEntity.ok().body(appointment);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponseModel> addAppointment(@RequestBody AppointmentRequestModel appointmentRequestModel) {

        AppointmentResponseModel appointment = appointmentService.addAppointment(appointmentRequestModel);
        if (appointment != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentResponseModel> updateAppointment(
            @PathVariable String appointmentId,
            @RequestBody AppointmentRequestModel appointmentRequestModel
    ) {
        try {
            AppointmentResponseModel appointment = appointmentService.updateAppointment(appointmentId, appointmentRequestModel);
            return ResponseEntity.ok().body(appointment);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/appointments/{appointmentId}")
    public ResponseEntity<Void> deleteAppointmentByAppointmentId(@PathVariable String appointmentId) {
        try {
            appointmentService.deleteAppointmentByAppointmentId(appointmentId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
