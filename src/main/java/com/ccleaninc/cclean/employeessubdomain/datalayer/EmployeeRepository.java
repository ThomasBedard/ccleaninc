package com.ccleaninc.cclean.employeessubdomain.datalayer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ccleaninc.cclean.employeessubdomain.datalayer.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

        // 1. Find employee by employee identifier
        Optional<Employee> findByEmployeeIdentifier_EmployeeId(String employeeId);

        // 2. Find employee by exact email (unique)
        // Employee findByEmail(String email);

        Employee findByEmail(String email);
    
        // 3. Find employees by phone number, allowing partial matches
        List<Employee> findByPhoneNumberContaining(String phoneNumber);
    
        // 4. Search by first name, last name, or email (partial match)
        @Query("SELECT e FROM Employee e WHERE " +
               "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
               "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
               "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
        List<Employee> searchByNameOrEmail(@Param("searchTerm") String searchTerm);
    
}