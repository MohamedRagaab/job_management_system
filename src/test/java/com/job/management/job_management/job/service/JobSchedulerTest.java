package com.job.management.job_management.job.service;

import com.job.management.job_management.job.entity.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class JobSchedulerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobScheduler jobScheduler;

    private List<Job> jobs;

    @BeforeEach
    public void setup() {
        jobs = List.of(new Job());  // Create a dummy job list
    }

    @Test
    public void testScheduleJobs() {
        when(jobService.getQueuedJobsReadyForExecution()).thenReturn(jobs);

        jobScheduler.scheduleJobs();

        verify(jobService, times(1)).getQueuedJobsReadyForExecution();
        verify(jobService, times(1)).executeJobs(jobs, jobService);
    }
}

