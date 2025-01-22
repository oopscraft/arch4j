package org.chomookun.arch4j.batch.common.item.file;

import org.chomookun.arch4j.batch.common.item.file.transform.FieldConversionService;
import org.chomookun.arch4j.batch.common.item.file.transform.FixedByteLineTokenizer;
import org.chomookun.arch4j.batch.common.item.file.transform.ItemTypeDescriptor;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;

public class FixedByteFileItemReader<T> extends GenericFileItemReader<T> {


    @Override
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
        FixedByteLineTokenizer lineTokenizer = new FixedByteLineTokenizer(getEncoding(), itemTypeDescriptor);

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
