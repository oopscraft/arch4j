package org.chomookun.arch4j.batch.common.item.file.transform;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ItemTypeDescriptor {

    @Getter
    private final Class<?> itemType;

    @Getter
    private final List<Field> fields = new ArrayList<>();

    public ItemTypeDescriptor(Class<?> itemType) {
        this.itemType = itemType;
        ReflectionUtils.doWithFields(itemType, fields::add);
    }

    public List<String> getFieldNames() {
        return fields.stream()
                .map(Field::getName)
                .toList();
    }

    public Field getField(int index) {
        return fields.get(index);
    }

    public TypeDescriptor getFieldType(int index) {
        return new TypeDescriptor(fields.get(index));
    }

}
