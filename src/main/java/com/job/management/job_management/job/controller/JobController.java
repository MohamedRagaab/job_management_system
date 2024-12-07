package com.job.management.job_management.job.controller;

import com.job.management.job_management.common.CustomResponse;
import com.job.management.job_management.job.entity.Job;
import com.job.management.job_management.job.entity.enums.JobStatus;
import com.job.management.job_management.job.service.JobService;
import com.job.management.job_management.job.util.JobMessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

    // API to retrieve a job by its ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Job>> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        CustomResponse<Job> response = new CustomResponse<>(
                true,
                JobMessageConstants.JOB_RETRIEVED_SUCCESSFULLY,
                job
        );
        return ResponseEntity.ok(response);
    }

    // API to retrieve jobs by status
    @GetMapping("/status/{status}")
    public ResponseEntity<CustomResponse<List<Job>>> getJobsByStatus(@PathVariable JobStatus status) {
        List<Job> jobs = jobService.getJobsByStatus(status);
        CustomResponse<List<Job>> response = new CustomResponse<>(
                true,
                JobMessageConstants.JOBS_RETRIEVED_SUCCESSFULLY,
                jobs
        );
        return ResponseEntity.ok(response);
    }

    // API to create a single job
    @PostMapping
    public ResponseEntity<CustomResponse<Job>> createJob(@RequestBody Job jobRequest) {
        Job job = jobService.createJob(jobRequest);
        CustomResponse<Job> response = new CustomResponse<>(
                true,
                JobMessageConstants.JOB_CREATED_SUCCESSFULLY,
                job
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // API to create batch jobs
    @PostMapping("/batch")
    public ResponseEntity<CustomResponse<List<Job>>> createBatchJobs(@RequestBody List<Job> jobRequests) {
        List<Job> jobs = jobService.createBatchJobs(jobRequests);
        CustomResponse<List<Job>> response = new CustomResponse<>(
                true,
                JobMessageConstants.JOBS_CREATED_SUCCESSFULLY,
                jobs
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // API to execute a job immediately by its ID
    @PostMapping("/{id}/execute")
    public ResponseEntity<CustomResponse<Job>> executeJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id); // Fetch the job
        jobService.executeJob(job); // Execute the job immediately
        CustomResponse<Job> response = new CustomResponse<>(
                true,
                JobMessageConstants.JOB_EXECUTED_SUCCESSFULLY,
                job
        );
        return ResponseEntity.ok(response);
    }

    // API to delete a single job
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Job>> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        CustomResponse<Job> response = new CustomResponse<>(
                true,
                JobMessageConstants.JOB_DELETED_SUCCESSFULLY,
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
