package org.oopscraft.arch4j.core.role;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;

import java.util.ArrayList;
import java.util.List;
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

    public static Role from(RoleEntity roleEntity){
        return Role.builder()
                .roleId(roleEntity.getRoleId())
                .roleName(roleEntity.getRoleName())
                .note(roleEntity.getNote())
                .authorities(roleEntity.getAuthorities().stream()
                        .map(Authority::from)
                        .collect(Collectors.toList()))
                .build();
    }
    
}
