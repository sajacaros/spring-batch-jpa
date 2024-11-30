package com.study.batch.jpa.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

public class ItemConvertProcessor<I, O> implements ItemProcessor<I, O> {
    private final Class<O> targetClass;

    public ItemConvertProcessor(Class<O> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public O process(I item) throws Exception {
        try {
            O target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(item, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert item", e);
        }
    }
}
