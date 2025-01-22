package org.chomookun.arch4j.batch.common.item.file;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericFileItemWriter<T> extends FlatFileItemWriter<T> {

    private final Map<Class<? extends T>,LineAggregator<T>> lineAggregatorRegistry = new HashMap<>();

    @Getter
    protected String encoding = "UTF-8";

    @Setter
    @Getter
    protected Class<? extends T> itemType;

    @Setter
    @Getter
    protected String lineSeparator = String.valueOf('\n');

    protected abstract LineAggregator<T> createLineAggregator(Class<? extends T> itemType);

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
