package com.ccleaninc.cclean.schedulestest;
import com.ccleaninc.cclean.schedulessubdomain.businesslayer.ScheduleService;
import com.ccleaninc.cclean.schedulessubdomain.datalayer.Schedule;
import com.ccleaninc.cclean.schedulessubdomain.datalayer.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSchedules() {
        Schedule schedule = new Schedule();
        when(scheduleRepository.findAll()).thenReturn(Collections.singletonList(schedule));

        List<Schedule> schedules = scheduleService.getAllSchedules();
        assertEquals(1, schedules.size());
        assertEquals(schedule, schedules.get(0));
    }
}
