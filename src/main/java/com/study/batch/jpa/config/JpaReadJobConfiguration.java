package com.study.batch.jpa.config;

import com.study.batch.jpa.domain.BatchJobExecution;
import com.study.batch.jpa.domain.BatchJobExecutionComposite;
import com.study.batch.jpa.processor.ItemConvertProcessor;
import com.study.batch.jpa.processor.LocalDateTimeToStringProcessor;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;

@Configuration
@Slf4j
public class JpaReadJobConfiguration {
    /**
     * CHUNK 크기를 지정한다.
     */
    public static final int CHUNK_SIZE = 2;
    public static final String ENCODING = "UTF-8";
    public static final String JPA_PAGING_CHUNK_JOB = "JPA_PAGING_CHUNK_JOB";

    private final EntityManagerFactory entityManagerFactory;

    public JpaReadJobConfiguration(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public JpaPagingItemReader<BatchJobExecution> batchJobExecutionJpaPagingItemReader() throws Exception {
        JpaPagingItemReader<BatchJobExecution> jpaPagingItemReader = new JpaPagingItemReader<>();
        jpaPagingItemReader.setQueryString(
                "SELECT je FROM BatchJobExecution je WHERE je.exitCode = :exitCode order by je.lastUpdated desc"
        );
        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
        jpaPagingItemReader.setPageSize(CHUNK_SIZE);
        jpaPagingItemReader.setParameterValues(Collections.singletonMap("exitCode", ExitStatus.FAILED.getExitCode()));
        return jpaPagingItemReader;
    }

//    @Bean
//    public JpaPagingItemReader<BatchJobExecution> batchJobExecutionJpaPagingItemReaderBuilder() throws Exception {
//        return new JpaPagingItemReaderBuilder<BatchJobExecution>()
//                .queryString("SELECT je FROM BatchJobExecution je WHERE je.exitCode = :exitCode order by je.lastUpdated desc")
//                .entityManagerFactory(entityManagerFactory)
//                .pageSize(CHUNK_SIZE)
//                .parameterValues(Collections.singletonMap("exitCode", ExitStatus.FAILED))
//                .build();
//    }

    @Bean
    public ItemProcessor<BatchJobExecution, BatchJobExecution> batchJobExecutionJpaItemProcessor() {
        return item -> {
            log.info("Item Processor ------------- {}", item);
            return item;
        };
    }

    @Bean
    public CompositeItemProcessor<BatchJobExecution, BatchJobExecutionComposite> compositeItemProcessor() {
        return new CompositeItemProcessorBuilder<BatchJobExecution, BatchJobExecutionComposite>()
                .delegates(List.of(
                        new ItemConvertProcessor<BatchJobExecution, BatchJobExecutionComposite>(BatchJobExecutionComposite.class),
                        new LocalDateTimeToStringProcessor<BatchJobExecutionComposite>()
                ))
                .build();
    }

    @Bean
    public FlatFileItemWriter<BatchJobExecutionComposite> batchJobExecutionJpaFlatFileItemWriter() {
        return new FlatFileItemWriterBuilder<BatchJobExecutionComposite>()
                .name("customerJpaFlatFileItemWriter")
                .resource(new FileSystemResource("./output/batch_failure.csv"))
                .encoding(ENCODING)
                .delimited().delimiter(",")
                .names("jobExecutionId", "version", "jobInstanceId", "createTimeString", "startTimeString",
                        "endTimeString", "status", "exitCode", "lastUpdatedString")
                .headerCallback(writer -> writer.write("JobExecutionId,Version,JobInstanceId,CreateTime,StartTime," +
                        "EndTime,Status,ExitCode,LastUpdated"))
                .build();
    }


    @Bean
    public Step batchJobExecutionJpaPagingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        log.info("------------------ Init batchJobExecutionJJpaPagingStep -----------------");

        return new StepBuilder("jobExecutionJpaPagingStep", jobRepository)
                .<BatchJobExecution, BatchJobExecutionComposite>chunk(CHUNK_SIZE, transactionManager)
                .reader(batchJobExecutionJpaPagingItemReader())
                .processor(compositeItemProcessor())
                .writer(batchJobExecutionJpaFlatFileItemWriter())
                .build();
    }

    @Bean
    public Job batchJobExecutionJpaPagingJob(Step batchJobExecutionJpaPagingStep, JobRepository jobRepository) {
        log.info("------------------ Init batchJobExecutionJpaPagingJob -----------------");
        return new JobBuilder(JPA_PAGING_CHUNK_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(batchJobExecutionJpaPagingStep)
                .build();
    }
}
