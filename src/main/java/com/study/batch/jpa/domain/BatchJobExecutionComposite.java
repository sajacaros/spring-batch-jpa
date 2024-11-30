package com.study.batch.jpa.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.BatchStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "batch_job_execution")
public class BatchJobExecutionComposite {
    @Id
    private Long jobExecutionId;
    private Long version;
    private Long jobInstanceId;
    private LocalDateTime createTime;
    private String createTimeString;
    private LocalDateTime startTime;
    private String startTimeString;
    private LocalDateTime endTime;
    private String endTimeString;
    @Enumerated(EnumType.STRING)
    private BatchStatus status;
    private String exitCode;
    private String exitMessage;
    private LocalDateTime lastUpdated;
    private String lastUpdatedString;

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
