// package com.ccleaninc.cclean.appointmentssubdomain.datalayer;

// import java.time.LocalDate;
// import java.time.LocalTime;

// import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceIdentifier;

// import jakarta.persistence.*;
// import lombok.*;

// @Entity
// @Table(name = "appointments")
// @Data
// @Builder
// @AllArgsConstructor
// @NoArgsConstructor
// public class Appointment {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Integer id;

//     @Embedded
//     private AppointmentIdentifier appointmentIdentifier;

//      @ManyToOne
//     @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = true, foreignKey = @ForeignKey(name = "FK_customer_appointments"))
//     private Customer customer;

//     @Column(name = "scheduled_date", nullable = false)
//     private LocalDate scheduledDate;

//     @Column(name = "scheduled_time", nullable = false)
//     private LocalTime scheduledTime;

//     @Enumerated(EnumType.STRING)
//     @Column(name = "status", nullable = false)
//     private AppointmentStatus status = AppointmentStatus.PENDING;

//     @Column(name = "notes", columnDefinition = "TEXT")
//     private String notes;

//     @Column(name = "service_ids", columnDefinition = "JSON", nullable = false)
//     private String serviceIds; // Store JSON array as a String
    
// }
