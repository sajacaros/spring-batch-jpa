package com.study.batch.jpa.writer;

import com.study.batch.jpa.service.BatchExecutionFailedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BatchExecutionFailedWriter<T> implements ItemWriter<T> {
    @Autowired
    private BatchExecutionFailedService batchExecutionFailedService;

    @Override
    public void write(Chunk<? extends T> chunk) throws Exception {
        for(T batchJobExecution: chunk) {
            log.info("Call Process in BatchExecutionFailedWriter...");
            batchExecutionFailedService.processToOtherService(batchJobExecution);
        }
    }
}
