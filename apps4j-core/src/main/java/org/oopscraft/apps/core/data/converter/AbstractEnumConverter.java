package org.oopscraft.apps.core.data.converter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

@Slf4j
public abstract class AbstractEnumConverter<E extends Enum<E>> implements AttributeConverter<E,String> {

    private Class<E> enumType;

    public AbstractEnumConverter() {
        this.enumType = detectEnumType();
    }

    private Class<E> detectEnumType() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<E>) type.getActualTypeArguments()[0];
    }
    @Override
    public String convertToDatabaseColumn(E attribute) {
        return Optional.ofNullable(attribute)
                .map(v->v.name())
                .orElse(null);
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        try {
            return Enum.valueOf((Class<E>) enumType, dbData);
        }catch(Throwable t){
            log.warn(t.getMessage());
            return null;
        }
    }

}
