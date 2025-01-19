package com.ccleaninc.cclean.appointmentssubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Appointment findAppointmentByAppointmentIdentifier_AppointmentId(String appointmentId);

    @Query("SELECT a FROM Appointment a WHERE a.customerId = :customerId")
    List<Appointment> findAllByCustomerId(@Param("customerId") String customerId);
}
