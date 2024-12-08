package com.ccleaninc.cclean.servicesubdomain.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
@Value
@Builder
@AllArgsConstructor
public class ServiceRequestModel {

    private String title;
    private String description;
    private BigDecimal pricing;
    private String category;
    private Integer durationMinutes;
}
