package com.ccleaninc.cclean.servicesubdomain.datalayer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "booking_sessions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customer_id")
    private String customerId;

    @ElementCollection
    @CollectionTable(name = "booking_services", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "service_id")
    private List<String> serviceIds;

    @Column(name = "selected_date")
    private LocalDate selectedDate;

    @Column(name = "selected_time")
    private String selectedTime;
}
