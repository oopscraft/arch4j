package org.oopscraft.arch4j.batch.common.item.file;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.core.io.Resource;

import java.util.Optional;

@Setter
@Accessors(chain = true, fluent = true)
public class FixedByteFileItemReaderBuilder<T> {

    private Class<? extends T> itemType;

    private Resource resource;

    private String encoding;

    private String lineSeparator;

    public FixedByteFileItemReader<T> build() {
        FixedByteFileItemReader<T> instance = new FixedByteFileItemReader<>();
        Optional.ofNullable(itemType).ifPresent(instance::setItemType);
        Optional.ofNullable(resource).ifPresent(instance::setResource);
        Optional.ofNullable(encoding).ifPresent(instance::setEncoding);
        Optional.ofNullable(lineSeparator).ifPresent(instance::setLineSeparator);
        return instance;
    }

}
