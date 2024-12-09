package com.ccleaninc.cclean.schedulessubdomain.datalayer;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "schedules")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "schedule_id", unique = true, nullable = false)
    private String scheduleId;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "service_id", nullable = false)
    private String serviceId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "location", nullable = false)
    private String location;
}
