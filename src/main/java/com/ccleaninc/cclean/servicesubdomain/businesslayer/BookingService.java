package com.ccleaninc.cclean.servicesubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    void saveSelectedServices(String customerId, List<String> serviceIds);
    void saveSelectedDate(String customerId, LocalDate selectedDate);
    void saveSelectedTime(String customerId, String selectedTime);
    AppointmentResponseModel confirmBooking(String customerId);
}
