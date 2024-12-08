package com.ccleaninc.cclean.servicesubdomain.datalayer;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "services")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private ServiceIdentifier serviceIdentifier;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "pricing", nullable = false)
    private BigDecimal pricing;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;


    @Column(name = "category")
    private String category;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

}
