package com.ccleaninc.cclean.schedulessubdomain.presentationlayer;

import com.ccleaninc.cclean.schedulessubdomain.businesslayer.ScheduleService;
import com.ccleaninc.cclean.schedulessubdomain.datalayer.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }
}