package org.oopscraft.arch4j.batch.item.file;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.core.io.Resource;

import java.util.Optional;

@Setter
@Accessors(chain = true, fluent = true)
public class DelimitedFileItemReaderBuilder<T> {

    public Resource resource;

    public String encoding;

    public Class<? extends T> itemType;

    public String lineSeparator;

    public String delimiter;

    public DelimitedFileItemReader<T> build() {
        DelimitedFileItemReader<T> instance = new DelimitedFileItemReader<>();
        Optional.ofNullable(resource).ifPresent(instance::setResource);
        Optional.ofNullable(encoding).ifPresent(instance::setEncoding);
        Optional.ofNullable(itemType).ifPresent(instance::setItemType);
        Optional.ofNullable(lineSeparator).ifPresent(instance::setLineSeparator);
        Optional.ofNullable(delimiter).ifPresent(instance::setDelimiter);
        return instance;
    }

}
