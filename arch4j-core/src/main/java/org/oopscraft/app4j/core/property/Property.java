package org.oopscraft.app4j.core.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.oopscraft.app4j.core.data.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "property")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Property extends BaseEntity {

    @Id
    @Column(name = "id", length = 64)
    private String id;
    
    @Column(name = "name")
    private String name;


}
