package org.chomookun.arch4j.batch.common.item.file.transform.converter;

import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Builder
public class DefaultDeserializer implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        ConvertiblePair[] pairs = new ConvertiblePair[] {
                new ConvertiblePair(String.class, Object.class),
        };
        Set<ConvertiblePair> pairSet = new HashSet<>(Arrays.asList(pairs));
        return Collections.unmodifiableSet(pairSet);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if(source == null || StringUtils.isEmpty(source.toString())) {
            return null;
        }
        return source.toString();
    }
}
