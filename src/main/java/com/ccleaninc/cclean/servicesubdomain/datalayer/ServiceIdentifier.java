package com.ccleaninc.cclean.servicesubdomain.datalayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ServiceIdentifier {
    @Column(name = "service_id", nullable = false, unique = true, length = 36)
    private String serviceId;

    public ServiceIdentifier() {
        this.serviceId = java.util.UUID.randomUUID().toString();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
