package org.oopscraft.arch4j.core.user;

import lombok.*;
import org.oopscraft.arch4j.core.user.dao.RoleAuthorityEntity;
import org.oopscraft.arch4j.core.user.dao.RoleEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role {

    private String roleId;

    private String roleName;

    private String note;

    @Builder.Default
	private List<Authority> authorities = new ArrayList<>();

    public static Role from(RoleEntity roleEntity) {
        Role role = Role.builder()
                .roleId(roleEntity.getRoleId())
                .roleName(roleEntity.getRoleName())
                .build();

        List<Authority> authorities = roleEntity.getRoleAuthorityEntities().stream()
                .map(RoleAuthorityEntity::getAuthorityEntity)
                .filter(Objects::nonNull)
                .map(Authority::from)
                .collect(Collectors.toList());

        role.setAuthorities(authorities);
        return role;
    }

}
