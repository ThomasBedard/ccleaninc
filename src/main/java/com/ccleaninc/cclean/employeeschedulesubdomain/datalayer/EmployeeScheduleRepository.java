package com.ccleaninc.cclean.employeeschedulesubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmployeeScheduleRepository extends JpaRepository<EmployeeSchedule, Integer> {

    // Find schedule by schedule identifier
    Optional<EmployeeSchedule> findByEmployeeScheduleIdentifier_ScheduleId(String scheduleId);

    // Find all schedules for a specific employee
    List<EmployeeSchedule> findByEmployeeId(String employeeId);

    // Find schedules by availability ID (relationship with Availability)
    List<EmployeeSchedule> findByAvailabilityId(String availabilityId);

    // Find schedules by assigned date
    List<EmployeeSchedule> findByAssignedDate(LocalDateTime assignedDate);

    /**
     * Advanced Search:
     * This query allows searching schedules based on:
     * - Employee's first name
     * - Last name
     * - Phone number
     * - Email
     * - Assigned date
     */
    @Query("SELECT s FROM EmployeeSchedule s " +
    "JOIN s.employee e " +  // Reference the mapped relationship
    "WHERE (LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "OR LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "OR e.phoneNumber LIKE CONCAT('%', :searchTerm, '%')) " +
    "OR (CAST(s.assignedDate AS string) LIKE CONCAT('%', :searchTerm, '%'))")
    List<EmployeeSchedule> searchSchedulesAdvanced(@Param("searchTerm") String searchTerm);
}
