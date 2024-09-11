package org.oopscraft.arch4j.core.security.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseModel;
import org.oopscraft.arch4j.core.security.dao.RoleAuthorityEntity;
import org.oopscraft.arch4j.core.security.dao.RoleEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role extends BaseModel {

    private String roleId;

    private String name;

    private boolean anonymous;

    private boolean authenticated;

    private String note;

    @Builder.Default
	private List<Authority> authorities = new ArrayList<>();

    /**
     * role factory method
     * @param roleEntity role entity
     * @return role
     */
    public static Role from(RoleEntity roleEntity) {
        Role role = Role.builder()
                .systemRequired(roleEntity.isSystemRequired())
                .systemUpdatedAt(roleEntity.getSystemUpdatedAt())
                .systemUpdatedBy(roleEntity.getSystemUpdatedBy())
                .roleId(roleEntity.getRoleId())
                .name(roleEntity.getName())
                .anonymous(roleEntity.isAnonymous())
                .authenticated(roleEntity.isAuthenticated())
                .note(roleEntity.getNote())
                .build();

        // authorities
        List<Authority> authorities = roleEntity.getRoleAuthorities().stream()
                .map(RoleAuthorityEntity::getAuthorityEntity)
                .filter(Objects::nonNull)
                .map(Authority::from)
                .collect(Collectors.toList());
        role.setAuthorities(authorities);

        // return
        return role;
    }

}
