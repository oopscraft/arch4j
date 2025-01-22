package org.chomookun.arch4j.batch.common.item.file;

import org.chomookun.arch4j.batch.common.item.file.transform.FieldConversionService;
import org.chomookun.arch4j.batch.common.item.file.transform.FixedByteLineAggregator;
import org.chomookun.arch4j.batch.common.item.file.transform.ItemTypeDescriptor;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;

public class FixedByteFileItemWriter<T> extends GenericFileItemWriter<T> {

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
        return new FixedByteLineAggregator<>(encoding, itemTypeDescriptor, fieldExtractor, conversionService);
    }

}
