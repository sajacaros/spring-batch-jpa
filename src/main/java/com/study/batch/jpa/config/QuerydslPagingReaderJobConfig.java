package com.study.batch.jpa.config;

import com.study.batch.jpa.domain.BatchJobExecution;
import com.study.batch.jpa.domain.QBatchJobExecution;
import com.study.batch.jpa.reader.QuerydslPagingItemReader;
import com.study.batch.jpa.reader.QuerydslPagingItemReaderBuilder;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class QuerydslPagingReaderJobConfig {
    /**
     * CHUNK 크기를 지정한다.
     */
    public static final int CHUNK_SIZE = 2;
    public static final String ENCODING = "UTF-8";
    public static final String QUERYDSL_PAGING_CHUNK_JOB = "QUERYDSL_PAGING_CHUNK_JOB";

    @Autowired
    DataSource dataSource;

    @Autowired
    EntityManagerFactory entityManagerFactory;

//    @Bean
//    public QuerydslPagingItemReader<BatchJobExecution> customerQuerydslPagingItemReader() throws Exception {
//        Function<JPAQueryFactory, JPAQuery<BatchJobExecution>> query = jpaQueryFactory -> jpaQueryFactory.select(QBatchJobExecution.batchJobExecution).from(QBatchJobExecution.batchJobExecution).where(QBatchJobExecution.batchJobExecution.status.eq(BatchStatus.FAILED));
//        return new QuerydslPagingItemReader<>("customerQuerydslPagingItemReader", entityManagerFactory, query, CHUNK_SIZE, false);
//    }

    @Bean
    public QuerydslPagingItemReader<BatchJobExecution> batchJobExecutionQuerydslPagingItemReader() {
        return new QuerydslPagingItemReaderBuilder<BatchJobExecution>()
                .name("batchJobExecutionQuerydslPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .chunkSize(2)
                .querySupplier(jpaQueryFactory -> jpaQueryFactory.select(QBatchJobExecution.batchJobExecution).from(QBatchJobExecution.batchJobExecution).where(QBatchJobExecution.batchJobExecution.status.eq(BatchStatus.FAILED)))
                .build();
    }

    @Bean
    public FlatFileItemWriter<BatchJobExecution> batchJobExecutionQuerydslFlatFileItemWriter() {
        return new FlatFileItemWriterBuilder<BatchJobExecution>()
                .name("batchJobExecutionQuerydslFlatFileItemWriter")
                .resource(new FileSystemResource("./output/batch_failure_v2.csv"))
                .encoding(ENCODING)
                .delimited().delimiter(",")
                .names("jobExecutionId", "version", "jobInstanceId", "createTime", "startTime",
                        "endTime", "status", "exitCode", "lastUpdated")
                .build();
    }


    @Bean
    public Step batchJobExecutionQuerydslPagingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        log.info("------------------ Init batchJobExecutionQuerydslPagingStep -----------------");

        return new StepBuilder("batchJobExecutionQuerydslPagingStep", jobRepository)
                .<BatchJobExecution, BatchJobExecution>chunk(CHUNK_SIZE, transactionManager)
                .reader(batchJobExecutionQuerydslPagingItemReader())
                .writer(batchJobExecutionQuerydslFlatFileItemWriter())
                .build();
    }

    @Bean
    public Job batchJobExecutionQuerydslPagingJob(Step batchJobExecutionQuerydslPagingStep, JobRepository jobRepository) {
        log.info("------------------ Init batchJobExecutionQuerydslPagingJob -----------------");
        return new JobBuilder(QUERYDSL_PAGING_CHUNK_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(batchJobExecutionQuerydslPagingStep)
                .build();
    }
}
