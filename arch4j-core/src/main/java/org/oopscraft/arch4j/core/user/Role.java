package org.oopscraft.arch4j.core.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.user.entity.RoleEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RoleEntity(group of authorities)
 */
@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class Role {

    private String id;
    
    private String name;

    private String icon;
    
    private String note;

    @Builder.Default
	List<Authority> authorities = new ArrayList<>();

    public static Role from(RoleEntity roleEntity){
        return Role.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName())
                .icon(roleEntity.getIcon())
                .note(roleEntity.getNote())
                .authorities(roleEntity.getAuthorities().stream()
                        .map(Authority::from)
                        .collect(Collectors.toList()))
                .build();
    }
    
}
