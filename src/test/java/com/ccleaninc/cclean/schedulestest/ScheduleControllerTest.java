package com.ccleaninc.cclean.schedulestest;

import com.ccleaninc.cclean.schedulessubdomain.businesslayer.ScheduleService;
import com.ccleaninc.cclean.schedulessubdomain.datalayer.Schedule;
import com.ccleaninc.cclean.schedulessubdomain.presentationlayer.ScheduleController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSchedulesReturnsSchedules() {
        Schedule schedule = new Schedule();
        when(scheduleService.getAllSchedules()).thenReturn(Collections.singletonList(schedule));

        ResponseEntity<List<Schedule>> response = scheduleController.getAllSchedules();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(schedule, response.getBody().get(0));
    }

    @Test
    void getAllSchedulesReturnsEmptyListWhenNoSchedulesExist() {
        when(scheduleService.getAllSchedules()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Schedule>> response = scheduleController.getAllSchedules();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());
    }


}