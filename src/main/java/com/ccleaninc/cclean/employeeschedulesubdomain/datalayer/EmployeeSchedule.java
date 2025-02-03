package com.ccleaninc.cclean.employeeschedulesubdomain.datalayer;

import java.time.LocalDateTime;

import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Shift;
import com.ccleaninc.cclean.employeessubdomain.datalayer.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_schedules")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSchedule {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private EmployeeScheduleIdentifier employeeScheduleIdentifier;

    @Column(name = "employee_id")
    private String employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", insertable = false, updatable = false)
    private Employee employee;

    @Column(name = "availability_id")
    private String availabilityId; // References Availability

    @Column(name = "assigned_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime assignedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift")
    private Shift shift; // MORNING, EVENING, NIGHT

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ScheduleStatus status; // PENDING, APPROVED, CANCELED

    private String comments;

    public EmployeeSchedule(String employeeId,
                            String availabilityId,
                            LocalDateTime assignedDate,
                            Shift shift,
                            ScheduleStatus status,
                            String comments) {
        this.employeeScheduleIdentifier = new EmployeeScheduleIdentifier();
        this.employeeId = employeeId;
        this.availabilityId = availabilityId;
        this.assignedDate = assignedDate;
        this.shift = shift;
        this.status = status;
        this.comments = comments;
    }  
}
