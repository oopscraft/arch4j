package org.oopscraft.arch4j.core.menu;

import lombok.*;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.role.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Menu {

    private String menuId;

    private String menuName;

    private String parentMenuId;

    private String parentMenuName;

    private String link;

    private MenuTarget target;

    private String icon;

    private Integer sort;

    private String note;

    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    public static Menu from(MenuEntity menuEntity) {
        MenuBuilder menuBuilder = Menu.builder()
                .menuId(menuEntity.getMenuId())
                .menuName(menuEntity.getMenuName())
                .parentMenuId(menuEntity.getParentMenuId())
                .link(menuEntity.getLink())
                .target(menuEntity.getTarget())
                .icon(menuEntity.getIcon())
                .sort(menuEntity.getSort())
                .note(menuEntity.getNote());

        MenuEntity parentMenuEntity = menuEntity.getParentMenu();
        if (parentMenuEntity != null) {
            menuBuilder.parentMenuName(parentMenuEntity.getMenuName());
        }

        List<Role> roles = menuEntity.getRoles().stream()
                .map(Role::from)
                .collect(Collectors.toList());
        menuBuilder.roles(roles);

        return menuBuilder.build();
    }

}
