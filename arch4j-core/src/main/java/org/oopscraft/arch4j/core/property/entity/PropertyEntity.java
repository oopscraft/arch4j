package org.oopscraft.arch4j.core.property.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;

@Entity
@Table(name = "property")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id", length = 64)
    private String id;
    
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "note")
    @Lob
    private String note;

}
