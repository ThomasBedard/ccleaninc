package com.ccleaninc.cclean.customerssubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Integer>{

    // Find customer by customer identifier
    Optional<Customer> findByCustomerIdentifier_CustomerId(String customerId);

    // Search by email (unique)
    Customer findByEmail(String email);

    // Search by phone number
    List<Customer> findByPhoneNumberContaining(String phoneNumber);

    // Search by first name, last name, or company name (partial matches)
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Customer> searchByNameOrCompany(@Param("searchTerm") String searchTerm);

    Optional<Customer> findOptionalByEmail(String email);
    
}
