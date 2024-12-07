package com.job.management.job_management.job.service;

import com.job.management.job_management.job.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobScheduler {

    private static final Logger log = LoggerFactory.getLogger(JobScheduler.class);

    private final JobService jobService;

    public JobScheduler(JobService jobService) {
        this.jobService = jobService;
    }

    // Periodically check for scheduled jobs
    @Scheduled(fixedRate = 10000) // Every 10 seconds
    public void scheduleJobs() {
        log.info("Checking for jobs to schedule...");

        // Fetch jobs with status QUEUED or FAILED, ordered by priority
        List<Job> jobs = jobService.getQueuedJobsReadyForExecution();
        log.info("Found {} jobs to process", jobs.size());

        // Process jobs
        jobService.executeJobs(jobs, jobService);
    }
}
