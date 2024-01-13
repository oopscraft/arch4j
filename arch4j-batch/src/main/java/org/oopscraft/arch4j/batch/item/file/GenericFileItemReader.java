package org.oopscraft.arch4j.batch.item.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericFileItemReader<T> extends FlatFileItemReader<T> {

    private final Map<Class<? extends T>, LineMapper<T>> lineMapperRegistry = new HashMap<>();

    @Setter
    @Getter
    public Class<? extends T> itemType;

    @Setter
    @Getter
    public String lineSeparator = String.valueOf('\n');

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
