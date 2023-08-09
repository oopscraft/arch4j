package org.oopscraft.arch4j.core.user.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_authority")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorityEntity extends SystemFieldEntity {

    @Id
    @Column(name = "authority_id", length = 32)
    private String authorityId;

    @Column(name = "authority_name")
    private String authorityName;

    @Column(name = "note")
    @Lob
    private String note;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "authority_id", updatable = false)
    @Builder.Default
    private List<RoleAuthorityEntity> roleAuthorityEntities = new ArrayList<>();

}
