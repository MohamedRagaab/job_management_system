package com.job.management.job_management.job.repository;

import com.job.management.job_management.job.entity.Job;
import com.job.management.job_management.job.entity.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    @Query("SELECT j FROM Job j WHERE (j.status = :status OR j.status = :failedStatus) " +
            "AND j.scheduledAt < CURRENT_TIMESTAMP AND j.attempts < 3 ORDER BY j.priority DESC")
    List<Job> findQueuedJobsReadyForExecution(@Param("status") JobStatus status,
                                              @Param("failedStatus") JobStatus failedStatus);
}
