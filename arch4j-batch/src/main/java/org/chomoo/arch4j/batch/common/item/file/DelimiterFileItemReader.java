package org.chomoo.arch4j.batch.common.item.file;

import lombok.Getter;
import lombok.Setter;
import org.chomoo.arch4j.batch.common.item.file.transform.DelimiterLineTokenizer;
import org.chomoo.arch4j.batch.common.item.file.transform.FieldConversionService;
import org.chomoo.arch4j.batch.common.item.file.transform.ItemTypeDescriptor;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;

public class DelimiterFileItemReader<T> extends GenericFileItemReader<T> {

    @Setter
    @Getter
    public String delimiter = String.valueOf(',');

    protected LineMapper<T> createLineMapper(Class<? extends T> itemType) {
        // item type descriptor
        ItemTypeDescriptor itemTypeDescriptor = new ItemTypeDescriptor(itemType);

        // conversion service
        FieldConversionService conversionService = FieldConversionService.builder()
                .dateTimeFormatter(dateTimeFormatter)
                .dateFormatter(dateFormatter)
                .timeFormatter(timeFormatter)
                .build();

        // line tokenizer
        DelimiterLineTokenizer lineTokenizer = new DelimiterLineTokenizer(itemTypeDescriptor);
        lineTokenizer.setDelimiter(delimiter);

        // field set mapper
        BeanWrapperFieldSetMapper<T> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(itemType);
        fieldSetMapper.setConversionService(conversionService);

        // line mapper
        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        // return
        return lineMapper;
    }


}
