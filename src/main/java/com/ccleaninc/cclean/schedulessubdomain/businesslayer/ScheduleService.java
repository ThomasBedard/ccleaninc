package com.ccleaninc.cclean.schedulessubdomain.businesslayer;

import com.ccleaninc.cclean.schedulessubdomain.datalayer.Schedule;
import com.ccleaninc.cclean.schedulessubdomain.datalayer.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
}