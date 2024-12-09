package com.study.batch.jpa.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBatchJobExecutionComposite is a Querydsl query type for BatchJobExecutionComposite
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBatchJobExecutionComposite extends EntityPathBase<BatchJobExecutionComposite> {

    private static final long serialVersionUID = -1603173245L;

    public static final QBatchJobExecutionComposite batchJobExecutionComposite = new QBatchJobExecutionComposite("batchJobExecutionComposite");

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final StringPath createTimeString = createString("createTimeString");

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final StringPath endTimeString = createString("endTimeString");

    public final StringPath exitCode = createString("exitCode");

    public final StringPath exitMessage = createString("exitMessage");

    public final NumberPath<Long> jobExecutionId = createNumber("jobExecutionId", Long.class);

    public final NumberPath<Long> jobInstanceId = createNumber("jobInstanceId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastUpdated = createDateTime("lastUpdated", java.time.LocalDateTime.class);

    public final StringPath lastUpdatedString = createString("lastUpdatedString");

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final StringPath startTimeString = createString("startTimeString");

    public final EnumPath<org.springframework.batch.core.BatchStatus> status = createEnum("status", org.springframework.batch.core.BatchStatus.class);

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QBatchJobExecutionComposite(String variable) {
        super(BatchJobExecutionComposite.class, forVariable(variable));
    }

    public QBatchJobExecutionComposite(Path<? extends BatchJobExecutionComposite> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBatchJobExecutionComposite(PathMetadata metadata) {
        super(BatchJobExecutionComposite.class, metadata);
    }

}

