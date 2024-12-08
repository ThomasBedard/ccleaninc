package com.ccleaninc.cclean.schedulestest;

import com.ccleaninc.cclean.schedulessubdomain.datalayer.Schedule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleTest {

    @Test
    void testScheduleGettersAndSetters() {
        Schedule schedule = new Schedule();
        UUID id = UUID.randomUUID();
        String scheduleId = "SCH123";
        String employeeId = "EMP456";
        String serviceId = "SER789";
        String customerId = "CUS012";
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);
        String status = "Scheduled";
        String location = "123 Main St";

        schedule.setId(id);
        schedule.setScheduleId(scheduleId);
        schedule.setEmployeeId(employeeId);
        schedule.setServiceId(serviceId);
        schedule.setCustomerId(customerId);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setStatus(status);
        schedule.setLocation(location);

        assertEquals(id, schedule.getId());
        assertEquals(scheduleId, schedule.getScheduleId());
        assertEquals(employeeId, schedule.getEmployeeId());
        assertEquals(serviceId, schedule.getServiceId());
        assertEquals(customerId, schedule.getCustomerId());
        assertEquals(startTime, schedule.getStartTime());
        assertEquals(endTime, schedule.getEndTime());
        assertEquals(status, schedule.getStatus());
        assertEquals(location, schedule.getLocation());
    }
}