package org.oopscraft.arch4j.core.user.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "core_role")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleEntity extends SystemFieldEntity {

    @Id
    @Column(name = "role_id", length = 32)
    private String roleId;

    @NotNull
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "note")
    @Lob
    private String note;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "role_id",
            referencedColumnName = "role_id",
            updatable = false
    )
    @Builder.Default
    private List<RoleAuthorityEntity> roleAuthorityEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "role_id",
            referencedColumnName = "role_id",
            updatable = false
    )
    private List<UserRoleEntity> userRoleEntities = new ArrayList<>();

}
