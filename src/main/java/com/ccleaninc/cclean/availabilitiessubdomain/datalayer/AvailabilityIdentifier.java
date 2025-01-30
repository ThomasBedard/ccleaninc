package com.ccleaninc.cclean.availabilitiessubdomain.datalayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Embeddable
@Data
@AllArgsConstructor
public class AvailabilityIdentifier {

    @Column(name = "availability_id", nullable = false, unique = true, length = 36)
    private String availabilityId;

    public AvailabilityIdentifier() {
        this.availabilityId = java.util.UUID.randomUUID().toString();
    }

    public void setAvailabilityId(String availabilityId) {
        this.availabilityId = availabilityId;
    } 
}
