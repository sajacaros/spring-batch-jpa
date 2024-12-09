package com.study.batch.jpa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface BatchExecutionFailedService<T> {
    Map<String, String> processToOtherService(T batchJobExecution);

    @Service
    @Slf4j
    class Default<T> implements BatchExecutionFailedService<T> {

        @Override
        public Map<String, String> processToOtherService(T batchJobExecution) {
            log.info("Call API in BatchExecutionFailedService...");
            return Map.of("code", "200", "message", "OK");
        }
    }
}
