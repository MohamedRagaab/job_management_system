package com.job.management.job_management.job.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.management.job_management.job.entity.Job;
import com.job.management.job_management.job.entity.enums.JobStatus;
import com.job.management.job_management.job.service.JobService;
import com.job.management.job_management.job.util.JobMessageConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobService jobService;

    private static final String DEFAULT_DESCRIPTION = "Default description for Test Job";
    private static final String DEFAULT_EXECUTION_PATH = "./executableTasks/HiSafeyTask.jar";
    private static final boolean DEFAULT_IMMEDIATE_EXECUTION = false;

    private Job createDefaultJob(String name) {
        Job job = new Job();
        job.setName(name);
        job.setDescription(DEFAULT_DESCRIPTION);
        job.setExecutableJarFilePath(DEFAULT_EXECUTION_PATH);
        job.setImmediateExecution(DEFAULT_IMMEDIATE_EXECUTION);
        return job;
    }

    @Test
    public void testCreateJob() throws Exception {
        Job job = createDefaultJob("E2E Test Job");

        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(job)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(JobMessageConstants.JOB_CREATED_SUCCESSFULLY))
                .andExpect(jsonPath("$.data.name").value(job.getName()))
                .andExpect(jsonPath("$.data.description").value(job.getDescription()))
                .andExpect(jsonPath("$.data.executableJarFilePath").value(job.getExecutableJarFilePath()))
                .andExpect(jsonPath("$.data.immediateExecution").value(job.isImmediateExecution()));
    }

    @Test
    public void testCreateBatchJobs() throws Exception {
        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            jobs.add(createDefaultJob("Batch Job " + i));
        }

        mockMvc.perform(post("/jobs/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(jobs)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(JobMessageConstants.JOBS_CREATED_SUCCESSFULLY))
                .andExpect(jsonPath("$.data[0].name").value("Batch Job 0"));
    }

    @Test
    public void testGetJobById() throws Exception {
        Job job = jobService.createJob(createDefaultJob("Get Job Test"));

        mockMvc.perform(get("/jobs/{id}", job.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(JobMessageConstants.JOB_RETRIEVED_SUCCESSFULLY))
                .andExpect(jsonPath("$.data.id").value(job.getId()))
                .andExpect(jsonPath("$.data.name").value(job.getName()))
                .andExpect(jsonPath("$.data.description").value(job.getDescription()))
                .andExpect(jsonPath("$.data.executableJarFilePath").value(job.getExecutableJarFilePath()));
    }

    @Test
    public void testGetJobById_JobNotFound() throws Exception {
        mockMvc.perform(get("/jobs/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Job with ID 999 not found"));
    }

    @Test
    public void testDeleteJob() throws Exception {
        Job job = jobService.createJob(createDefaultJob("Job to Delete"));

        mockMvc.perform(delete("/jobs/{id}", job.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(JobMessageConstants.JOB_DELETED_SUCCESSFULLY));

        assertThrows(Exception.class, () -> jobService.getJobById(job.getId()));
    }

    @Test
    public void testDeleteJob_JobRunning() throws Exception {
        Job job = jobService.createJob(createDefaultJob("Running Job"));

        // Simulate running status
        jobService.updateJobStatus(job, JobStatus.RUNNING);

        mockMvc.perform(delete("/jobs/{id}", job.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot delete a running job with ID " + job.getId()));
    }

    @Test
    public void testExecuteJob() throws Exception {
        // Creating a new job for execution
        Job job = createDefaultJob("Execute Job Test");
        job.setImmediateExecution(true);
        job = jobService.createJob(job);

        // Perform the job execution
        mockMvc.perform(post("/jobs/{id}/execute", job.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(JobMessageConstants.JOB_EXECUTED_SUCCESSFULLY))
                .andExpect(jsonPath("$.data.id").value(job.getId()))
                .andExpect(jsonPath("$.data.name").value(job.getName()))
                .andExpect(jsonPath("$.data.description").value(job.getDescription()))
                .andExpect(jsonPath("$.data.executableJarFilePath").value(job.getExecutableJarFilePath()))
                .andExpect(jsonPath("$.data.immediateExecution").value(job.isImmediateExecution()))
                .andExpect(jsonPath("$.data.attempts").value(job.getAttempts() + 1))
                .andExpect(jsonPath("$.data.status").value(JobStatus.SUCCESS.name()))
                .andExpect(jsonPath("$.data.priority").value(job.getPriority()))
                .andExpect(jsonPath("$.data.scheduledAt").exists());
    }
}
