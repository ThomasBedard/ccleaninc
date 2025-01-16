package com.ccleaninc.cclean.appointmentssubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Appointment findAppointmentByAppointmentIdentifier_AppointmentId(String appointmentId);
}
