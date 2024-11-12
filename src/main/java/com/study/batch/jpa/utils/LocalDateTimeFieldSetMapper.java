package com.study.batch.jpa.utils;

import com.study.batch.jpa.domain.BatchJobExecutionBackup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class LocalDateTimeFieldSetMapper implements FieldSetMapper<BatchJobExecutionBackup> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");


    @Override
    public BatchJobExecutionBackup mapFieldSet(FieldSet fieldSet) throws BindException {
        log.info("------------ {}", fieldSet.readString("CreateTime"));

        BatchJobExecutionBackup item = new BatchJobExecutionBackup();
        item.setBackupId(UUID.randomUUID().toString());
        item.setJobExecutionId(fieldSet.readLong("JobExecutionId"));
        item.setVersion(fieldSet.readLong("Version"));
        item.setJobInstanceId(fieldSet.readLong("JobInstanceId"));
        item.setCreateTime(LocalDateTime.parse(fieldSet.readString("CreateTime"), FORMATTER));
        item.setStartTime(LocalDateTime.parse(fieldSet.readString("StartTime"), FORMATTER));
        item.setEndTime(LocalDateTime.parse(fieldSet.readString("EndTime"), FORMATTER));
        item.setStatus(BatchStatus.valueOf(fieldSet.readString("Status")));
        item.setExitCode(fieldSet.readString("ExitCode"));
        item.setLastUpdated(LocalDateTime.parse(fieldSet.readString("LastUpdated"), FORMATTER));
        return item;
    }
}
