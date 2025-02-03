package com.ccleaninc.cclean.employeeschedulesubdomain.datalayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@Data
@AllArgsConstructor
public class EmployeeScheduleIdentifier {

    @Column(name = "schedule_id", nullable = false, unique = true, length = 36)
    private String scheduleId;

    public EmployeeScheduleIdentifier() {
        this.scheduleId = java.util.UUID.randomUUID().toString();
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
}
