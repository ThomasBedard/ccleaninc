package com.ccleaninc.cclean.availabilitiessubdomain.datalayer;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_schedule_availabilities")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Availability {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private AvailabilityIdentifier availabilityIdentifier;

    @Column(name = "employee_first_name")
    private String employeeFirstName;

    @Column(name = "employee_last_name")
    private String employeeLastName;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "available_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime availableDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift")
    private Shift shift;

    private String comments;

    public Availability(String employeeId,
                        LocalDateTime availableDate,
                        Shift shift,
                        String comments) {
        this.availabilityIdentifier = new AvailabilityIdentifier();
        this.employeeId = employeeId;
        this.availableDate = availableDate;
        this.shift = shift;
        this.comments = comments;
    }
}
