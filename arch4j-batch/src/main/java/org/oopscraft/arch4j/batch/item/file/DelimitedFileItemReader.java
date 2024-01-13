package org.oopscraft.arch4j.batch.item.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.oopscraft.arch4j.batch.sample.dto.SampleFile;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

public class DelimitedFileItemReader<T> extends GenericFileItemReader<T> {

    @Setter
    @Getter
    public String delimiter = String.valueOf(',');

    protected LineMapper<T> createLineMapper(Class<? extends T> itemType) {
        // line tokenizer
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(delimiter);
        List<String> fieldNames = new ArrayList<>();
        ReflectionUtils.doWithFields(itemType, field -> fieldNames.add(field.getName()));
        lineTokenizer.setNames(fieldNames.toArray(new String[0]));

        // field set mapper
        BeanWrapperFieldSetMapper<T> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setConversionService(new DefaultConversionService());

        // line mapper
        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        // return
        return lineMapper;
    }


}
