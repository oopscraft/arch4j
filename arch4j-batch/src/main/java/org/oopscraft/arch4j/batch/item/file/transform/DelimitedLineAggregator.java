package org.oopscraft.arch4j.batch.item.file.transform;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.file.transform.ExtractorLineAggregator;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
public class DelimitedLineAggregator<T> extends ExtractorLineAggregator<T> {

    private final ItemTypeDescriptor itemTypeDescriptor;

    @Setter
    private ConversionService conversionService;

    @Setter
    private String delimiter = String.valueOf('\t');

    public DelimitedLineAggregator(ItemTypeDescriptor itemTypeDescriptor) {
        this.itemTypeDescriptor = itemTypeDescriptor;
    }

    @NotNull
    @Override
    public String doAggregate(@NotNull Object[] fields) {
        try {
            log.debug("[FIELD]{}", (Object)fields);
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

            // convert to line
            String line = StringUtils.arrayToDelimitedString(values.toArray(new String[0]), delimiter);
            log.debug("[LINE-CHAR][{}]\n", line);
            return line;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
