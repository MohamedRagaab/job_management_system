package com.job.management.job_management.job.entity;

import com.job.management.job_management.job.entity.enums.JobStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * The job entity representation.
 */
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    private String description;
    @NotNull
    private String executableJarFilePath;

    private boolean immediateExecution;

    private int attempts = 0;

    @NotNull
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.QUEUED;

    private Integer priority = 0;
    private ZonedDateTime scheduledAt;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getScheduledAt() {
        return scheduledAt;
    }

    public JobStatus getStatus() {
        return status;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public String getExecutableJarFilePath() {
        return executableJarFilePath;
    }

    public boolean isImmediateExecution() {
        return immediateExecution;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String payload) {
        this.description = payload;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setScheduledAt(ZonedDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public void setExecutableJarFilePath(String executableJarFilePath) {
        this.executableJarFilePath = executableJarFilePath;
    }

    public void setImmediateExecution(boolean immediateExecution) {
        this.immediateExecution = immediateExecution;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", executableJarFilePath='" + executableJarFilePath + '\'' +
                ", isImmediateExecution=" + immediateExecution +
                ", attempts=" + attempts +
                ", status=" + status +
                ", priority=" + priority +
                ", scheduledAt=" + scheduledAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
