package org.chomoo.arch4j.core.security.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.common.data.BaseEntity;
import org.chomoo.arch4j.core.common.data.converter.BooleanConverter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @Column(name = "name")
    private String name;

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
