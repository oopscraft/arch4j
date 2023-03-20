package org.oopscraft.arch4j.core.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldSupport;

import javax.persistence.*;

/**
 * Authority
 */
@Entity
@Table(name = "authority")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority extends SystemFieldSupport {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "icon")
    @Lob
    private String icon;
    
    @Column(name = "note")
    @Lob
    private String note;

}
