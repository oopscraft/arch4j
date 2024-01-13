package org.oopscraft.arch4j.batch.item.file;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;

public class FixedByteFileItemReader<T> extends GenericFileItemReader<T> {

    @Override
    protected LineMapper<T> createLineMapper(Class<? extends T> itemType) {
        return null;
    }
}
