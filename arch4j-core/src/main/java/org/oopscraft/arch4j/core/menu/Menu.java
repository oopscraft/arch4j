package org.oopscraft.arch4j.core.menu;

import lombok.*;
import org.oopscraft.arch4j.core.menu.repository.MenuEntity;
import org.oopscraft.arch4j.core.user.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    private String id;

    private String parentId;

    private String parentName;

    private String name;

    private String link;

    private String target;

    private String icon;

    private Integer sort;

    private String note;

    private List<Role> roles = new ArrayList<>();

    /**
     * factory method from menu entity
     * @param menuEntity menu entity
     * @return menu
     */
    public static Menu from(MenuEntity menuEntity) {
        return Menu.builder()
                .id(menuEntity.getId())
                .parentId(menuEntity.getParentId())
                .parentName(Optional.ofNullable(menuEntity.getParentMenu())
                        .map(MenuEntity::getName)
                        .orElse(null))
                .name(menuEntity.getName())
                .link(menuEntity.getLink())
                .target(menuEntity.getTarget())
                .icon(menuEntity.getIcon())
                .sort(menuEntity.getSort())
                .note(menuEntity.getNote())
                .roles(menuEntity.getRoles().stream()
                        .map(Role::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
