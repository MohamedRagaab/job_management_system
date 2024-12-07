package com.job.management.job_management.job.controller;

import com.job.management.job_management.common.CustomResponse;
import com.job.management.job_management.job.entity.Job;
import com.job.management.job_management.job.entity.enums.JobStatus;
import com.job.management.job_management.job.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    private Job job;

    @BeforeEach
    public void setup() {
        job = new Job();
        job.setId(1L);
        job.setName("TestJob");
        job.setStatus(JobStatus.QUEUED);
    }

    @Test
    public void testGetJobById() {
        when(jobService.getJobById(1L)).thenReturn(job);

        ResponseEntity<CustomResponse<Job>> response = jobController.getJobById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getData());
        assertEquals("TestJob", response.getBody().getData().getName());
        verify(jobService, times(1)).getJobById(1L);
    }

    @Test
    public void testGetJobsByStatus() {
        List<Job> jobs = List.of(new Job());
        when(jobService.getJobsByStatus(JobStatus.QUEUED)).thenReturn(jobs);

        ResponseEntity<CustomResponse<List<Job>>> response = jobController.getJobsByStatus(JobStatus.QUEUED);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().getData().isEmpty());
        verify(jobService, times(1)).getJobsByStatus(JobStatus.QUEUED);
    }

    @Test
    public void testCreateJob() {
        when(jobService.createJob(any(Job.class))).thenReturn(job);

        ResponseEntity<CustomResponse<Job>> response = jobController.createJob(job);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("TestJob", response.getBody().getData().getName());
        verify(jobService, times(1)).createJob(any(Job.class));
    }

    @Test
    public void testCreateBatchJobs() {
        List<Job> jobList = List.of(job);
        when(jobService.createBatchJobs(anyList())).thenReturn(jobList);

        ResponseEntity<CustomResponse<List<Job>>> response = jobController.createBatchJobs(jobList);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertFalse(response.getBody().getData().isEmpty());
        verify(jobService, times(1)).createBatchJobs(anyList());
    }

    @Test
    public void testDeleteJob() {
        doNothing().when(jobService).deleteJob(1L);

        ResponseEntity<CustomResponse<Job>> response = jobController.deleteJob(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(jobService, times(1)).deleteJob(1L);
    }
}
