package org.oopscraft.arch4j.core.property;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.property.entity.PropertyEntity;

import javax.persistence.*;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class Property {

    private String id;
    
    private String name;

    private String value;

    private String note;

    public static Property from(PropertyEntity propertyEntity) {
        return Property.builder()
                .id(propertyEntity.getId())
                .name(propertyEntity.getName())
                .value(propertyEntity.getValue())
                .note(propertyEntity.getNote())
                .build();
    }

}
