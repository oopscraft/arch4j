package org.oopscraft.arch4j.batch.item.file;

import org.oopscraft.arch4j.batch.item.file.transform.DelimitedLineAggregator;
import org.oopscraft.arch4j.batch.item.file.transform.FieldConversionService;
import org.oopscraft.arch4j.batch.item.file.transform.FixedLengthLineAggregator;
import org.oopscraft.arch4j.batch.item.file.transform.ItemTypeDescriptor;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;

public class FixedLengthFileItemWriter<T> extends GenericFileItemWriter<T> {

    @Override
    protected LineAggregator<T> createLineAggregator(Class<? extends T> itemType) {
        // item type descriptor
        ItemTypeDescriptor itemTypeDescriptor = new ItemTypeDescriptor(itemType);

        // conversion service
        FieldConversionService conversionService = FieldConversionService.builder()
                .dateTimeFormatter(dateTimeFormatter)
                .dateFormatter(dateFormatter)
                .timeFormatter(timeFormatter)
                .build();

        // field extractor
        BeanWrapperFieldExtractor<T> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(itemTypeDescriptor.getFieldNames().toArray(new String[0]));

        // line aggregator
        FixedLengthLineAggregator<T> lineAggregator = new FixedLengthLineAggregator<>(itemTypeDescriptor);
        lineAggregator.setConversionService(conversionService);
        lineAggregator.setFieldExtractor(fieldExtractor);

        // return
        return lineAggregator;
    }

}
