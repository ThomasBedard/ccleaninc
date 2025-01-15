package com.ccleaninc.cclean.servicesubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingSessionRepository extends JpaRepository<BookingSession, Integer> {
    BookingSession findByCustomerId(String customerId);
}
