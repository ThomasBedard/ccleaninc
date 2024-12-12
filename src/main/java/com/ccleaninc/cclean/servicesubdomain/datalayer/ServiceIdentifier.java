package com.ccleaninc.cclean.servicesubdomain.datalayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Setter
@Getter
@Embeddable
@Data
@AllArgsConstructor

public class ServiceIdentifier {
    @Column(name = "service_id", nullable = false, unique = true, length = 36)
    private String serviceId;

    public ServiceIdentifier() {
        this.serviceId = java.util.UUID.randomUUID().toString();
    }

}
