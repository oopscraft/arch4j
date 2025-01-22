package org.chomookun.arch4j.batch.common.item.file.transform;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.chomookun.arch4j.batch.common.item.file.transform.converter.*;
import org.springframework.core.convert.support.GenericConversionService;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class FieldConversionService extends GenericConversionService {

    @Setter
    @Getter
    protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Setter
    @Getter
    protected DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Setter
    @Getter
    protected DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    public FieldConversionService() {
        // dateTime type
        addConverter(DateSerializer.builder()
                .dateTimeFormatter(dateTimeFormatter)
                .dateFormatter(dateFormatter)
                .timeFormatter(timeFormatter)
                .build());
        addConverter(DateDeserializer.builder()
                .dateTimeFormatter(dateTimeFormatter)
                .dateFormatter(dateFormatter)
                .timeFormatter(timeFormatter)
                .build());

        // numeric type
        this.addConverter(NumericSerializer.builder()
                .build());
        this.addConverter(NumericDeserializer.builder()
                .build());

        // default fallback
        this.addConverter(DefaultSerializer.builder()
                .build());
        this.addConverter(DefaultDeserializer.builder()
                .build());
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class FieldConversionServiceBuilder {

        private DateTimeFormatter dateTimeFormatter;

        private DateTimeFormatter dateFormatter;

        private DateTimeFormatter timeFormatter;

        public FieldConversionService build() {
            FieldConversionService instance = new FieldConversionService();
            Optional.ofNullable(dateTimeFormatter).ifPresent(instance::setDateTimeFormatter);
            Optional.ofNullable(dateFormatter).ifPresent(instance::setDateFormatter);
            Optional.ofNullable(timeFormatter).ifPresent(instance::setTimeFormatter);
            return instance;
        }
    }

    public static FieldConversionServiceBuilder builder() {
        return new FieldConversionServiceBuilder();
    }

}

