package org.oopscraft.arch4j.batch.item.file;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.core.io.Resource;

import java.util.Optional;

@Setter
@Accessors(chain = true, fluent = true)
public class DelimitedFileItemWriterBuilder<T> {

    public Resource resource;

    public String encoding;

    public Class<? extends T> itemType;

    public String lineSeparator;

    public String delimiter;

    public DelimitedFileItemWriter<T> build() {
        DelimitedFileItemWriter<T> instance = new DelimitedFileItemWriter<>();
        Optional.ofNullable(resource).ifPresent(instance::setResource);
        Optional.ofNullable(encoding).ifPresent(instance::setEncoding);
        Optional.ofNullable(itemType).ifPresent(instance::setItemType);
        Optional.ofNullable(lineSeparator).ifPresent(instance::setLineSeparator);
        Optional.ofNullable(delimiter).ifPresent(instance::setDelimiter);
        return instance;
    }

}
