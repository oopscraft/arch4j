package org.oopscraft.arch4j.core.role.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.BaseEntity;
import org.oopscraft.arch4j.core.data.converter.BooleanConverter;
import org.oopscraft.arch4j.core.user.dao.UserRoleEntity;

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
public class RoleEntity extends BaseEntity {

    @Id
    @Column(name = "role_id", length = 32)
    private String roleId;

    @NotNull
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "anonymous", length = 1)
    @Convert(converter = BooleanConverter.class)
    private boolean anonymous;

    @Column(name = "authenticated", length = 1)
    @Convert(converter = BooleanConverter.class)
    private boolean authenticated;

    @Column(name = "note")
    @Lob
    private String note;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "role_id", updatable = false)
    @Builder.Default
    private List<RoleAuthorityEntity> roleAuthorities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "role_id", updatable = false)
    @Builder.Default
    private List<UserRoleEntity> userRoles = new ArrayList<>();

}
