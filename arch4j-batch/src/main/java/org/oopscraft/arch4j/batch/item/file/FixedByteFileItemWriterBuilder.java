package org.oopscraft.arch4j.batch.item.file;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.core.io.Resource;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Setter
@Accessors(chain = true, fluent = true)
public class FixedByteFileItemWriterBuilder<T> {

    private Class<? extends T> itemType;

    private Resource resource;

    private String encoding;

    private String lineSeparator;

    private DateTimeFormatter dateTimeFormatter;

    private DateTimeFormatter dateFormatter;

    private DateTimeFormatter timeFormatter;

    public FixedByteFileItemWriter<T> build() {
        FixedByteFileItemWriter<T> instance = new FixedByteFileItemWriter<>();
        Optional.ofNullable(itemType).ifPresent(instance::setItemType);
        Optional.ofNullable(resource).ifPresent(instance::setResource);
        Optional.ofNullable(encoding).ifPresent(instance::setEncoding);
        Optional.ofNullable(lineSeparator).ifPresent(instance::setLineSeparator);
        Optional.ofNullable(dateTimeFormatter).ifPresent(instance::setDateTimeFormatter);
        Optional.ofNullable(dateFormatter).ifPresent(instance::setDateFormatter);
        Optional.ofNullable(timeFormatter).ifPresent(instance::setTimeFormatter);
        return instance;
    }

}
