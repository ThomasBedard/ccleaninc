package com.ccleaninc.cclean.availabilitiessubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    
    // Find a specific availability by its identifier
    Availability findAvailabilityByAvailabilityIdentifier_AvailabilityId(String availabilityId);

    /*
     * JPQL operates on entities, not tables, 
     * so the table name in the database (employee_schedule_availabilities) 
     * does not need to match the entity name (Availability).
     * 
     * Hence, the SELECT a FROM Availability
     * instead of FROM employee_schedule_availabilities 
     */
    // Find all availabilities by employeeId
    @Query("SELECT a FROM Availability a WHERE a.employeeId = :employeeId")
    List<Availability> findAllByEmployeeId(@Param("employeeId") String employeeId);
}
