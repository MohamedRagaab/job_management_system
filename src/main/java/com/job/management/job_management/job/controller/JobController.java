package com.job.management.job_management.job.controller;

import com.job.management.job_management.common.CustomResponse;
import com.job.management.job_management.job.entity.Job;
import com.job.management.job_management.job.service.JobService;
import com.job.management.job_management.job.util.JobMessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
