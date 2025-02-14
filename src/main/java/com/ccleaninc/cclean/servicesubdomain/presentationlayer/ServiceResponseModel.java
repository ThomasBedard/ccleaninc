package com.ccleaninc.cclean.servicesubdomain.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
@Value
@Builder
@AllArgsConstructor
public class ServiceResponseModel {

    private Integer id;
    private String serviceId;
    private String title;
    private String description;
    private BigDecimal pricing;
    private Boolean isAvailable;
    private String category;
    private Integer durationMinutes;
    private String image;

}
