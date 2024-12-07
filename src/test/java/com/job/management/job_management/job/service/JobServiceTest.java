package com.job.management.job_management.job.service;

import com.job.management.job_management.job.entity.Job;
import com.job.management.job_management.job.entity.enums.JobStatus;
import com.job.management.job_management.job.repository.JobRepository;
import com.job.management.job_management.job.exception.JobNotFoundException;
import com.job.management.job_management.job.exception.JobDeletionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private Job job;

    @BeforeEach
    public void setup() {
        job = new Job();
        job.setId(1L);
        job.setName("TestJob");
        job.setName("TestJob Description..");
        job.setExecutableJarFilePath("./executableTasks/HiSafeyTask.jar");
        job.setImmediateExecution(true);
        job.setStatus(JobStatus.QUEUED);
        job.setAttempts(0);
    }

    @Test
    public void testCreateJob_ImmediateExecution() {
        job.setImmediateExecution(true);
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        Job createdJob = jobService.createJob(job);

        assertNotNull(createdJob);
        assertEquals(job.getStatus(), JobStatus.SUCCESS);
        verifyNoMoreInteractions(jobRepository);
    }

    @Test
    public void testCreateJob_JobSaveFailure() {
        job.setImmediateExecution(true);
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        job.setExecutableJarFilePath("null");
        Job createdJob = jobService.createJob(job);

        assertNotNull(createdJob);
        assertEquals(job.getStatus(), JobStatus.FAILED);
        verifyNoMoreInteractions(jobRepository);
    }

    @Test
    public void testGetJobById_JobFound() {
        when(jobRepository.findById(1L)).thenReturn(java.util.Optional.of(job));

        Job foundJob = jobService.getJobById(1L);

        assertNotNull(foundJob);
        assertEquals(job.getId(), foundJob.getId());
        verify(jobRepository, times(1)).findById(1L);

        verifyNoMoreInteractions(jobRepository);
    }

    @Test
    public void testGetJobById_JobNotFound() {
        when(jobRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(JobNotFoundException.class, () -> jobService.getJobById(1L));
    }

    @Test
    public void testDeleteJob_JobFound() {
        when(jobRepository.findById(1L)).thenReturn(java.util.Optional.of(job));

        jobService.deleteJob(1L);

        verify(jobRepository, times(1)).delete(job);
    }

    @Test
    public void testDeleteJob_JobRunning() {
        job.setStatus(JobStatus.RUNNING);
        when(jobRepository.findById(1L)).thenReturn(java.util.Optional.of(job));

        assertThrows(JobDeletionException.class, () -> jobService.deleteJob(1L));
    }
}
