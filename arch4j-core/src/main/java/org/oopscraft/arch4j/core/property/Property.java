package org.oopscraft.arch4j.core.property;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.property.repository.PropertyEntity;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class Property {

    private String id;

    private String value;

    private String name;

    private String note;

    public static Property from(PropertyEntity propertyEntity) {
        return Property.builder()
                .id(propertyEntity.getId())
                .value(propertyEntity.getValue())
                .name(propertyEntity.getName())
                .note(propertyEntity.getNote())
                .build();
    }

}
