package org.oopscraft.arch4j.batch.item.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericFileItemReader<T> extends FlatFileItemReader<T> {

    private final Map<Class<? extends T>, LineMapper<T>> lineMapperRegistry = new HashMap<>();

    @Getter
    private String encoding = "UTF-8";

    @Setter
    @Getter
    protected Class<? extends T> itemType;

    @Setter
    @Getter
    protected String lineSeparator = String.valueOf('\n');

    @Setter
    @Getter
    protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Setter
    @Getter
    protected DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Setter
    @Getter
    protected DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public void setEncoding(@NotNull String encoding) {
        this.encoding = encoding;
        super.setEncoding(encoding);
    }

    protected abstract LineMapper<T> createLineMapper(Class<? extends T> itemType);

    protected T doRead() throws Exception {
        // switch line mapper
        if(!lineMapperRegistry.containsKey(itemType)) {
            lineMapperRegistry.put(itemType, createLineMapper(itemType));
        }
        LineMapper<T> lineMapper = lineMapperRegistry.get(itemType);
        this.setLineMapper(lineMapper);

        // call super
        return super.doRead();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // creates line mapper
        LineMapper<T> lineMapper = createLineMapper(itemType);
        this.setLineMapper(lineMapper);
        lineMapperRegistry.put(itemType, lineMapper);

        // call super
        super.afterPropertiesSet();
    }

}
