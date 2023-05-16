package org.oopscraft.arch4j.core.role.repository;

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
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorityEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    @Lob
    private String note;

}
