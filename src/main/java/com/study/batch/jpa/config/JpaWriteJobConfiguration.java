package com.study.batch.jpa.config;

import com.study.batch.jpa.domain.BatchJobExecutionBackup;
import com.study.batch.jpa.utils.LocalDateTimeFieldSetMapper;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class JpaWriteJobConfiguration {
    /**
     * CHUNK 크기를 지정한다.
     */
    public static final int CHUNK_SIZE = 100;
    public static final String ENCODING = "UTF-8";
    public static final String JPA_ITEM_WRITE_JOB = "JPA_ITEM_WRITER_JOB";

    private final EntityManagerFactory entityManagerFactory;

    public JpaWriteJobConfiguration(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public FlatFileItemReader<BatchJobExecutionBackup> flatFileItemReader() {

        return new FlatFileItemReaderBuilder<BatchJobExecutionBackup>()
                .name("FlatFileItemReader")
                .resource(new FileSystemResource("./output/batch_failure.csv"))
                .encoding(ENCODING)
                .delimited().delimiter(",")
                .names("JobExecutionId", "Version", "JobInstanceId", "CreateTime", "StartTime",
                        "EndTime", "Status", "ExitCode", "LastUpdated")
                .linesToSkip(1)
                .fieldSetMapper(new LocalDateTimeFieldSetMapper())
                .build();
    }

    @Bean
    public JpaItemWriter<BatchJobExecutionBackup> jpaItemWriter() {
        return new JpaItemWriterBuilder<BatchJobExecutionBackup>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }


    @Bean
    public Step flatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init flatFileStep -----------------");

        return new StepBuilder("flatFileStep", jobRepository)
                .<BatchJobExecutionBackup, BatchJobExecutionBackup>chunk(CHUNK_SIZE, transactionManager)
                .reader(flatFileItemReader())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public Job flatFileJob(Step flatFileStep, JobRepository jobRepository) {
        log.info("------------------ Init flatFileJob -----------------");
        return new JobBuilder(JPA_ITEM_WRITE_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(flatFileStep)
                .build();
    }
}
