package org.oopscraft.arch4j.core.role;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.role.repository.RoleEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RoleEntity(group of authorities)
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    private String id;

    private String name;

    private String note;

    @Builder.Default
	List<Authority> authorities = new ArrayList<>();

    public static Role from(RoleEntity roleEntity){
        return Role.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName())
                .note(roleEntity.getNote())
                .authorities(roleEntity.getAuthorities().stream()
                        .map(Authority::from)
                        .collect(Collectors.toList()))
                .build();
    }
    
}
