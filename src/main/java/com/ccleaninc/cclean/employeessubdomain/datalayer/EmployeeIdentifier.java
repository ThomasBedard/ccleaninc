package com.ccleaninc.cclean.employeessubdomain.datalayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@Data
@AllArgsConstructor
public class EmployeeIdentifier {

    @Column(name = "employee_id", nullable = false, unique = true, length = 36)
    private String employeeId;

    public EmployeeIdentifier() {
        this.employeeId = java.util.UUID.randomUUID().toString();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
}
