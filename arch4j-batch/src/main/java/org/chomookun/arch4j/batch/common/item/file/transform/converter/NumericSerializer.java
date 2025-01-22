package org.chomookun.arch4j.batch.common.item.file.transform.converter;

import lombok.Builder;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Builder
public class NumericSerializer implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        ConvertiblePair[] pairs = new ConvertiblePair[] {
                new ConvertiblePair(Integer.class, String.class),
                new ConvertiblePair(Long.class, String.class),
                new ConvertiblePair(Double.class, String.class),
                new ConvertiblePair(Float.class, String.class),
                new ConvertiblePair(Number.class, String.class),
                new ConvertiblePair(BigDecimal.class, String.class)
        };
        Set<ConvertiblePair> pairSet = new HashSet<>(Arrays.asList(pairs));
        return Collections.unmodifiableSet(pairSet);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if(source == null) {
            return "";
        }
        return String.valueOf(source);
    }
}
