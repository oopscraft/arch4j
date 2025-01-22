package org.chomookun.arch4j.batch.common.item.file.transform;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.file.transform.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
public class DelimiterLineAggregator<T> implements LineAggregator<T> {

    protected final String encoding;

    protected final ItemTypeDescriptor itemTypeDescriptor;

    protected final FieldExtractor<T> fieldExtractor;

    protected final ConversionService conversionService;

    @Setter
    private String delimiter = String.valueOf('\t');

    public DelimiterLineAggregator(String encoding, ItemTypeDescriptor itemTypeDescriptor, FieldExtractor<T> fieldExtractor, ConversionService conversionService) {
        this.encoding = encoding;
        this.itemTypeDescriptor = itemTypeDescriptor;
        this.fieldExtractor = fieldExtractor;
        this.conversionService = conversionService;
    }

    @NotNull
    @Override
    public String aggregate(@NotNull T item) {
        Assert.notNull(item, "Item is required");
        Object[] fields = this.fieldExtractor.extract(item);

        log.debug("[FIELD]{}", (Object)fields);

        // convert fields
        List<String> values = new ArrayList<>();
        int i = -1;
        for(Object field : fields) {
            i ++;
            TypeDescriptor sourceType = itemTypeDescriptor.getFieldType(i);
            TypeDescriptor targetType = TypeDescriptor.valueOf(String.class);
            String value = Optional.ofNullable(conversionService.convert(field, sourceType, targetType))
                    .map(Object::toString)
                    .orElse("");
            log.debug("[{}][{}][{}]", i, field, value);
            values.add(value);
        }

        // aggregate line
        String line = StringUtils.arrayToDelimitedString(values.toArray(new String[0]), delimiter);
        log.debug("[LINE][{}]\n", line);
        return line;
    }


}
