package com.ccleaninc.cclean.servicesubdomain.datalayer;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    @Test
    void serviceConstructor_ShouldInitializeFieldsCorrectly() {
        String title = "Deep Cleaning";
        String description = "Comprehensive cleaning service";
        BigDecimal pricing = new BigDecimal("199.99");
        Boolean isAvailable = true;
        String category = "Cleaning";
        Integer durationMinutes = 120;

        Service service = new Service(title, description, pricing, isAvailable, category, durationMinutes);

        assertNotNull(service.getServiceIdentifier());
        assertEquals(title, service.getTitle());
        assertEquals(description, service.getDescription());
        assertEquals(pricing, service.getPricing());
        assertEquals(isAvailable, service.getIsAvailable());
        assertEquals(category, service.getCategory());
        assertEquals(durationMinutes, service.getDurationMinutes());
    }
}
