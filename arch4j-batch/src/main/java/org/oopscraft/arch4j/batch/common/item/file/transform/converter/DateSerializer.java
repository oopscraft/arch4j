package org.oopscraft.arch4j.batch.common.item.file.transform.converter;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
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
public class DateSerializer implements GenericConverter {

    @Builder.Default
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Builder.Default
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Builder.Default
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        ConvertiblePair[] pairs = new ConvertiblePair[] {
                new ConvertiblePair(LocalDateTime.class, String.class),
                new ConvertiblePair(LocalDate.class, String.class),
                new ConvertiblePair(LocalTime.class, String.class)
        };
        Set<ConvertiblePair> pairSet = new HashSet<>(Arrays.asList(pairs));
        return Collections.unmodifiableSet(pairSet);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

        if(source == null) {
            return "";
        }

        Class<?> sourceTypeClass = sourceType.getType();
        try {

            // java.time.LocalDateTime
            if (sourceTypeClass == LocalDateTime.class) {
                LocalDateTime localDateTime = (LocalDateTime)source;
                return dateTimeFormatter.format(localDateTime.withNano(0));
            }

            // java.time.LocalDate
            if (sourceTypeClass == LocalDate.class) {
                LocalDate localDate = (LocalDate)source;
                return dateFormatter.format(localDate);
            }

            // java.tile.LocalTime
            if (sourceTypeClass == LocalTime.class) {
                LocalTime localTime = (LocalTime)source;
                return timeFormatter.format(localTime);
            }

        }catch(Exception e){
            log.warn("Invalid value[{}]({}):{}", source, sourceTypeClass, e.getMessage());
            return "";
        }
        throw new RuntimeException("incompatible type");
    }

}
