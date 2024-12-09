package com.study.batch.jpa.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBatchJobExecution is a Querydsl query type for BatchJobExecution
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBatchJobExecution extends EntityPathBase<BatchJobExecution> {

    private static final long serialVersionUID = -379584924L;

    public static final QBatchJobExecution batchJobExecution = new QBatchJobExecution("batchJobExecution");

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

    public QBatchJobExecution(String variable) {
        super(BatchJobExecution.class, forVariable(variable));
    }

    public QBatchJobExecution(Path<? extends BatchJobExecution> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBatchJobExecution(PathMetadata metadata) {
        super(BatchJobExecution.class, metadata);
    }

}

