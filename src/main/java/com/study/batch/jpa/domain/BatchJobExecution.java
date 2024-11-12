package com.study.batch.jpa.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.springframework.batch.core.BatchStatus;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "batch_job_execution")
public class BatchJobExecution {
    @Id
    private Long jobExecutionId;
    private Long version;
    private Long jobInstanceId;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private BatchStatus status;
    private String exitCode;
    private String exitMessage;
    private LocalDateTime lastUpdated;

    @Override
    public String toString() {
        return "BatchJobExecution{" +
                "jobExecutionId=" + jobExecutionId +
                ", jobInstanceId=" + jobInstanceId +
                ", startTime=" + startTime +
                ", lastUpdated=" + lastUpdated +
                ", exitCode='" + exitCode +
                ", exitMessage='" + exitMessage.substring(0, 100) +
                '}';
    }
}
