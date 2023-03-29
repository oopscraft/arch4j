package org.oopscraft.arch4j.core.user.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;

/**
 * AuthorityEntity
 */
@Entity
@Table(name = "authority")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorityEntity extends SystemFieldEntity {

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
