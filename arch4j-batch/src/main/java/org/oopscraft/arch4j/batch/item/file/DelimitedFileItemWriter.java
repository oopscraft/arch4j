package org.oopscraft.arch4j.batch.item.file;

import lombok.Builder;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.batch.sample.dto.SampleFile;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

public class DelimitedFileItemWriter<T> extends GenericFileItemWriter<T> {

    @Setter
    public String delimiter = String.valueOf(',');

    @Override
    protected LineAggregator<T> createLineAggregator(Class<? extends T> itemType) {
        // field extractor
        BeanWrapperFieldExtractor<T> fieldExtractor = new BeanWrapperFieldExtractor<>();
        List<String> fieldNames = new ArrayList<>();
        ReflectionUtils.doWithFields(SampleFile.class, field -> fieldNames.add(field.getName()));
        fieldExtractor.setNames(fieldNames.toArray(new String[0]));

        // line aggregator
        DelimitedLineAggregator<T> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setFieldExtractor(fieldExtractor);
        lineAggregator.setDelimiter(delimiter);

        // return
        return lineAggregator;
    }

}
