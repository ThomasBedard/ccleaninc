package com.ccleaninc.cclean.employeeschedulesubdomain.presentationlayer;

import com.ccleaninc.cclean.employeeschedulesubdomain.businesslayer.EmployeeScheduleService;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/schedules")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeScheduleController {

    private final EmployeeScheduleService employeeScheduleService;

    // Get all employee schedules
    @GetMapping
    public ResponseEntity<List<EmployeeScheduleResponseModel>> getAllSchedules() {
        List<EmployeeScheduleResponseModel> schedules = employeeScheduleService.getAllSchedules();
        if (schedules == null || schedules.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(schedules);
    }

    // Create a new employee schedule
    @PostMapping
    public ResponseEntity<EmployeeScheduleResponseModel> createSchedule(@RequestBody EmployeeScheduleRequestModel requestModel) {
        try {
            EmployeeScheduleResponseModel createdSchedule = employeeScheduleService.createSchedule(requestModel);
            return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get schedule by ID
    @GetMapping("/{scheduleId}")
    public ResponseEntity<EmployeeScheduleResponseModel> getScheduleByScheduleId(@PathVariable String scheduleId) {
        try {
            EmployeeScheduleResponseModel schedule = employeeScheduleService.getScheduleByScheduleId(scheduleId);
            return ResponseEntity.ok(schedule);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Update an employee schedule
    @PutMapping("/{scheduleId}")
    public ResponseEntity<EmployeeScheduleResponseModel> updateSchedule(
            @PathVariable String scheduleId,
            @RequestBody EmployeeScheduleRequestModel requestModel) {
        try {
            EmployeeScheduleResponseModel updatedSchedule = employeeScheduleService.updateSchedule(scheduleId, requestModel);
            return ResponseEntity.ok(updatedSchedule);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delete a schedule by ID
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String scheduleId) {
        try {
            employeeScheduleService.deleteScheduleByScheduleId(scheduleId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get schedules by employee ID
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeScheduleResponseModel>> getSchedulesByEmployeeId(@PathVariable String employeeId) {
        try {
            List<EmployeeScheduleResponseModel> schedules = employeeScheduleService.getSchedulesByEmployeeId(employeeId);
            if (schedules.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(schedules);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Search schedules by employee name, phone, email, or assigned date
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeScheduleResponseModel>> searchSchedules(@RequestParam String searchTerm) {
        try {
            List<EmployeeScheduleResponseModel> schedules = employeeScheduleService.searchSchedules(searchTerm);
            if (schedules == null || schedules.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(schedules);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
