package org.oopscraft.arch4j.batch.item.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.jetbrains.annotations.NotNull;
import org.oopscraft.arch4j.batch.sample.dto.SampleFile;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.AbstractFileItemWriter;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericFileItemWriter<T> extends FlatFileItemWriter<T> {

    private final Map<Class<? extends T>,LineAggregator<T>> lineAggregatorRegistry = new HashMap<>();

    @Setter
    @Getter
    public Class<? extends T> itemType;

    @Setter
    @Getter
    public String lineSeparator = String.valueOf('\n');

    protected abstract LineAggregator<T> createLineAggregator(Class<? extends T> itemType);

    @NotNull
    public String doWrite(List<? extends T> items) {
        StringBuilder lines = new StringBuilder();
        for(T item : items) {
            @SuppressWarnings("unchecked")
            Class<? extends T> itemType = (Class<? extends T>) item.getClass();
            if(!lineAggregatorRegistry.containsKey(itemType)) {
                lineAggregatorRegistry.put(itemType, createLineAggregator(itemType));
            }
            LineAggregator<T> lineAggregator = lineAggregatorRegistry.get(itemType);
            lines.append(lineAggregator.aggregate(item)).append(lineSeparator);
        }
        return lines.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // create default line aggregator
        LineAggregator<T> lineAggregator = createLineAggregator(itemType);
        setLineAggregator(lineAggregator);
        lineAggregatorRegistry.put(itemType, lineAggregator);

        // call super
        super.afterPropertiesSet();
    }

}
