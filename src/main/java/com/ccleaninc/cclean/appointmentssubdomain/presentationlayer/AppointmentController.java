package com.ccleaninc.cclean.appointmentssubdomain.presentationlayer;

import com.ccleaninc.cclean.appointmentssubdomain.businesslayer.AppointmentService;
import com.ccleaninc.cclean.customerssubdomain.businesslayer.CustomerService;
import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerRepository;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
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

    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentResponseModel> getAppointmentByAppointmentId(@PathVariable String appointmentId) {
        try {
            AppointmentResponseModel appointment = appointmentService.getAppointmentByAppointmentId(appointmentId);
            return ResponseEntity.ok().body(appointment);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
    public ResponseEntity<AppointmentResponseModel> updateAppointment(@PathVariable String appointmentId, @RequestBody AppointmentRequestModel appointmentRequestModel) {
        AppointmentResponseModel appointment = appointmentService.updateAppointment(appointmentId, appointmentRequestModel);
        if (appointment != null) {
            return ResponseEntity.ok().body(appointment);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/appointments/{appointmentId}")
    public ResponseEntity<Void> deleteAppointmentByAppointmentId(@PathVariable String appointmentId) {
        try {
            appointmentService.deleteAppointmentByAppointmentId(appointmentId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/appointments/pdf")
    public ResponseEntity<byte[]> generateAppointmentsPdf() {
        ByteArrayOutputStream pdfData = appointmentService.generateAppointmentsPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "appointments.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData.toByteArray());
    }

}
