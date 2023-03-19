package org.oopscraft.arch4j.core.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldSupport;

import javax.persistence.*;

@Entity
@Table(name = "property")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Property extends SystemFieldSupport {

    @Id
    @Column(name = "id", length = 64)
    private String id;
    
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

}
