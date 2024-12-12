package com.ccleaninc.cclean.customerssubdomain.datalayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@Data
@AllArgsConstructor
public class CustomerIdentifier {

    @Column(name = "customer_id", nullable = false, unique = true, length = 36)
    private String customerId;

    public CustomerIdentifier() {
        this.customerId = java.util.UUID.randomUUID().toString();
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
