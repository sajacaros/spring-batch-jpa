package com.study.batch.jpa.config;

import com.study.batch.jpa.domain.BatchJobExecution;
import com.study.batch.jpa.domain.BatchJobExecutionComposite;
import com.study.batch.jpa.writer.BatchExecutionFailedWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class CustomItermWriterJobConfig {
    /**
     * CHUNK 크기를 지정한다.
     */
    public static final int CHUNK_SIZE = 100;
    public static final String ENCODING = "UTF-8";
    public static final String CUSTOM_ITEM_WRITER = "CUSTOM_ITEM_WRITER";

    @Autowired
    DataSource dataSource;

    @Autowired
    BatchExecutionFailedWriter customItemWriter;

    @Bean
    public FlatFileItemReader<BatchJobExecutionComposite> batchJobExecutionFlatFileItemReader() {

        return new FlatFileItemReaderBuilder<BatchJobExecutionComposite>()
                .name("batchJobExecutionFlatFileItemReader")
                .resource(new FileSystemResource("./output/batch_failure_v2.csv"))
                .encoding(ENCODING)
                .delimited().delimiter(",")
                .names("jobExecutionId", "version", "jobInstanceId", "createTimeString", "startTimeString",
                                        "endTimeString", "status", "exitCode", "lastUpdatedString")
                .targetType(BatchJobExecutionComposite.class)
                .build();
    }

    @Bean
    public Step customFlatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init customFlatFileStep -----------------");

        return new StepBuilder("customFlatFileStep", jobRepository)
                .<BatchJobExecutionComposite, BatchJobExecutionComposite>chunk(CHUNK_SIZE, transactionManager)
                .reader(batchJobExecutionFlatFileItemReader())
                .writer(customItemWriter)
                .build();
    }

    @Bean
    public Job customFlatFileJob(Step customFlatFileStep, JobRepository jobRepository) {
        log.info("------------------ Init customFlatFileJob -----------------");
        return new JobBuilder(CUSTOM_ITEM_WRITER, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(customFlatFileStep)
                .build();
    }
}
