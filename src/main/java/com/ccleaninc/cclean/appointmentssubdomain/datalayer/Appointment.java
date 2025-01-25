package com.ccleaninc.cclean.appointmentssubdomain.datalayer;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private AppointmentIdentifier appointmentIdentifier;

    private String customerFirstName;
    private String customerLastName;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "scheduled_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime appointmentDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    private String services;

    private String comments;


    public Appointment(String customerId, LocalDateTime appointmentDate, Status status, String comments) {
        this.appointmentIdentifier = new AppointmentIdentifier();
        this.customerId = customerId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.comments = comments;
    }


}
