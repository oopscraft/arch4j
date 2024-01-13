package org.oopscraft.arch4j.batch.item.file;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;

public class FixedByteFileItemWriter<T> extends GenericFileItemWriter<T> {

    @Override
    protected LineAggregator<T> createLineAggregator(Class<? extends T> itemType) {
        return null;
    }

}
