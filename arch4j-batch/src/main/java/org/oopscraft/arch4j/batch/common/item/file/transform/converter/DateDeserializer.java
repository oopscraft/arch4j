package org.oopscraft.arch4j.batch.common.item.file.transform.converter;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Builder
public class DateDeserializer implements GenericConverter {

    @Builder.Default
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Builder.Default
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Builder.Default
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        ConvertiblePair[] pairs = new ConvertiblePair[] {
                new ConvertiblePair(String.class, LocalDateTime.class),
                new ConvertiblePair(String.class, LocalDate.class),
                new ConvertiblePair(String.class, LocalTime.class),
        };
        Set<ConvertiblePair> pairSet = new HashSet<>(Arrays.asList(pairs));
        return Collections.unmodifiableSet(pairSet);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

        if(source == null || StringUtils.isEmpty(source.toString())) {
            return null;
        }

        Class<?> targetTypeClass = targetType.getType();
        try {

            // java.time.LocalDateTime
            if (targetTypeClass == LocalDateTime.class) {
                return LocalDateTime.parse(source.toString(), dateTimeFormatter)
                        .withNano(0);
            }

            // java.time.LocalDate
            if (targetTypeClass == LocalDate.class) {
                return LocalDate.parse(source.toString(), dateFormatter);
            }

            // java.time.LocalTime
            if (targetTypeClass == LocalTime.class) {
                return LocalTime.parse(source.toString(), timeFormatter);
            }

        }catch(Exception e){
            log.warn("Invalid value[{}]({}):{}", source, targetTypeClass, e.getMessage());
            return null;
        }
        throw new RuntimeException("incompatible type");
    }

}
