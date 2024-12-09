package com.study.batch.jpa.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBatchJobExecutionBackup is a Querydsl query type for BatchJobExecutionBackup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBatchJobExecutionBackup extends EntityPathBase<BatchJobExecutionBackup> {

    private static final long serialVersionUID = -1258420922L;

    public static final QBatchJobExecutionBackup batchJobExecutionBackup = new QBatchJobExecutionBackup("batchJobExecutionBackup");

    public final StringPath backupId = createString("backupId");

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final StringPath exitCode = createString("exitCode");

    public final StringPath exitMessage = createString("exitMessage");

    public final NumberPath<Long> jobExecutionId = createNumber("jobExecutionId", Long.class);

    public final NumberPath<Long> jobInstanceId = createNumber("jobInstanceId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastUpdated = createDateTime("lastUpdated", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final EnumPath<org.springframework.batch.core.BatchStatus> status = createEnum("status", org.springframework.batch.core.BatchStatus.class);

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QBatchJobExecutionBackup(String variable) {
        super(BatchJobExecutionBackup.class, forVariable(variable));
    }

    public QBatchJobExecutionBackup(Path<? extends BatchJobExecutionBackup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBatchJobExecutionBackup(PathMetadata metadata) {
        super(BatchJobExecutionBackup.class, metadata);
    }

}

