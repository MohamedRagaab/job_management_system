package com.job.management.job_management.job.service;

import com.job.management.job_management.job.entity.Job;
import com.job.management.job_management.job.entity.enums.JobStatus;
import com.job.management.job_management.job.repository.JobRepository;
import com.job.management.job_management.job.util.JobConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class JobService {
    private final JobRepository jobRepository;

    private static final int MAX_CONCURRENT_JOBS = JobConstants.MAX_CONCURRENT_JOBS; // Set the maximum number of concurrent jobs
    private static final int MAX_ATTEMPTS = JobConstants.MAX_ATTEMPTS; // Set the maximum number of attempts

    private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_CONCURRENT_JOBS);  // Thread pool

    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job createJob(Job job) {
        if (job.isImmediateExecution()) {
            job.setScheduledAt(ZonedDateTime.now());
            executeJob(job); // Delegate to executeJob
        }
        return jobRepository.save(job);
    }

    public void deleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("[deleteJob] Job not found"));
        if (job.getStatus() == JobStatus.RUNNING) {
            throw new IllegalStateException("[deleteJob] Cannot delete a running job");
        }
        jobRepository.delete(job);
    }

    public List<Job> getQueuedJobsReadyForExecution() {
        return jobRepository.findQueuedJobsReadyForExecution(JobStatus.QUEUED, JobStatus.FAILED);
    }

    public void updateJobStatus(Job job, JobStatus status) {
        job.setStatus(status);
        jobRepository.save(job);
    }

    public void executeJob(Job job) {
        if (job.getAttempts() >= MAX_ATTEMPTS) {  // Stop retrying if the max attempts are reached
            log.error("[executeJob] Job {} has exceeded max retry attempts", job.getId());
            return;
        }

        job.setAttempts(job.getAttempts() + 1); // Increment the attempt count
        updateJobStatus(job, JobStatus.RUNNING);

        try {
            Process process = new ProcessBuilder("java", "-jar", job.getExecutableJarFilePath()).start();
            int exitCode = process.waitFor();

            JobStatus finalStatus = exitCode == 0 ? JobStatus.SUCCESS : JobStatus.FAILED;
            updateJobStatus(job, finalStatus);
        } catch (IOException | InterruptedException e) {
            updateJobStatus(job, JobStatus.FAILED);
            log.error("[executeJob] Error executing job {}: {}", job.getId(), e.getMessage());
        }
    }

    public void executeJobs(List<Job> jobs, JobService jobService) {
        // Process jobs in batches of MAX_CONCURRENT_JOBS
        for (int i = 0; i < jobs.size(); i += MAX_CONCURRENT_JOBS) {
            int end = Math.min(i + MAX_CONCURRENT_JOBS, jobs.size());
            List<Job> batch = jobs.subList(i, end);

            log.info("[executeJobs] Executing batch #{}:", i);
            // Submit each job in the batch to be processed concurrently
            for (Job job : batch) {
                executorService.submit(() -> {
                    try {
                        jobService.executeJob(job);
                        log.info("[executeJobs] Executing job {}:", job.getId());
                    } catch (Exception e) {
                        log.error("[executeJobs] Error executing job {}: {}", job.getId(), e.getMessage());
                    }
                });
            }
        }
    }
}