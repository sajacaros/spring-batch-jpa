package com.study.batch.jpa.processor;

import org.springframework.batch.item.ItemProcessor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeToStringProcessor<T> implements ItemProcessor<T, T> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public T process(T item) throws Exception {
        Class<?> clazz = item.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.getType() == LocalDateTime.class) {
                String stringFieldName = field.getName() + "String";

                try {
                    // LocalDateTime 필드 접근
                    field.setAccessible(true);
                    LocalDateTime dateTimeValue = (LocalDateTime) field.get(item);

                    if (dateTimeValue != null) {
                        // 해당하는 String 필드 찾기
                        Field stringField = clazz.getDeclaredField(stringFieldName);
                        stringField.setAccessible(true);

                        // LocalDateTime을 String으로 변환하여 설정
                        String formattedDate = dateTimeValue.format(formatter);
                        stringField.set(item, formattedDate);
                    }
                } catch (NoSuchFieldException e) {
                    // String 필드가 없는 경우 무시
                    continue;
                }
            }
        }

        return item;
    }
}
