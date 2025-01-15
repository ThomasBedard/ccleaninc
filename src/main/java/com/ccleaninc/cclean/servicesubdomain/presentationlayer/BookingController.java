package com.ccleaninc.cclean.servicesubdomain.presentationlayer;

import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;
import com.ccleaninc.cclean.servicesubdomain.businesslayer.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/select-services")
    public ResponseEntity<String> selectServices(@RequestParam String customerId, @RequestBody List<String> serviceIds) {
        bookingService.saveSelectedServices(customerId, serviceIds);
        return ResponseEntity.ok("Services selected.");
    }

    @PostMapping("/select-date")
    public ResponseEntity<String> selectDate(@RequestParam String customerId, @RequestParam String date) {
        bookingService.saveSelectedDate(customerId, LocalDate.parse(date));
        return ResponseEntity.ok("Date selected.");
    }

    @PostMapping("/select-time")
    public ResponseEntity<String> selectTime(@RequestParam String customerId, @RequestParam String time) {
        bookingService.saveSelectedTime(customerId, time);
        return ResponseEntity.ok("Time selected.");
    }

    @PostMapping("/confirm")
    public ResponseEntity<AppointmentResponseModel> confirmBooking(@RequestParam String customerId) {
        AppointmentResponseModel appointment = bookingService.confirmBooking(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }
}
