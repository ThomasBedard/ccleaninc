package com.ccleaninc.cclean.servicesubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.AppointmentRepository;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;
import com.ccleaninc.cclean.servicesubdomain.datalayer.BookingSession;
import com.ccleaninc.cclean.servicesubdomain.datalayer.BookingSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingSessionRepository bookingSessionRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public void saveSelectedServices(String customerId, List<String> serviceIds) {
        BookingSession session = bookingSessionRepository.findByCustomerId(customerId);
        if (session == null) {
            session = BookingSession.builder()
                    .customerId(customerId)
                    .serviceIds(serviceIds)
                    .build();
        } else {
            session.setServiceIds(serviceIds);
        }
        bookingSessionRepository.save(session);
    }

    @Override
    public void saveSelectedDate(String customerId, LocalDate selectedDate) {
        BookingSession session = bookingSessionRepository.findByCustomerId(customerId);
        session.setSelectedDate(selectedDate);
        bookingSessionRepository.save(session);
    }

    @Override
    public void saveSelectedTime(String customerId, String selectedTime) {
        BookingSession session = bookingSessionRepository.findByCustomerId(customerId);
        session.setSelectedTime(selectedTime);
        bookingSessionRepository.save(session);
    }

    @Override
    public AppointmentResponseModel confirmBooking(String customerId) {
        BookingSession session = bookingSessionRepository.findByCustomerId(customerId);
        if (session == null) throw new RuntimeException("No booking session found.");

        Appointment appointment = Appointment.builder()
                .customerId(customerId)
                .appointmentDate(LocalDateTime.of(session.getSelectedDate(), LocalTime.parse(session.getSelectedTime())))
                .services(String.join(",", session.getServiceIds()))
                .status(Status.pending)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        bookingSessionRepository.delete(session);  // Clear session after booking

        return AppointmentResponseModel.builder()
                .appointmentId(savedAppointment.getAppointmentIdentifier().getAppointmentId())
                .customerId(savedAppointment.getCustomerId())
                .appointmentDate(savedAppointment.getAppointmentDate())
                .services(savedAppointment.getServices())
                .status(savedAppointment.getStatus())
                .build();
    }
}
