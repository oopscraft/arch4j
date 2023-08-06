package org.oopscraft.arch4j.core.menu;

import lombok.*;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.menu.dao.MenuRoleEntity;
import org.oopscraft.arch4j.core.user.Role;
import org.oopscraft.arch4j.core.security.SecurityPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private SecurityPolicy viewPolicy;

    @Builder.Default
    private List<Role> viewRoles = new ArrayList<>();

    private SecurityPolicy linkPolicy;

    @Builder.Default
    private List<Role> linkRoles = new ArrayList<>();

    public static Menu from(MenuEntity menuEntity) {
        Menu menu = Menu.builder()
                .menuId(menuEntity.getMenuId())
                .menuName(menuEntity.getMenuName())
                .parentMenuId(menuEntity.getParentMenuId())
                .link(menuEntity.getLink())
                .target(menuEntity.getTarget())
                .icon(menuEntity.getIcon())
                .sort(menuEntity.getSort())
                .note(menuEntity.getNote())
                .build();

        MenuEntity parentMenuEntity = menuEntity.getParentMenu();
        if (parentMenuEntity != null) {
            menu.setParentMenuName(parentMenuEntity.getMenuName());
        }

        // view role
        menu.setViewPolicy(menuEntity.getViewPolicy());
        List<Role> viewRoles = menuEntity.getViewMenuRoleEntities().stream()
                .map(MenuRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        menu.setViewRoles(viewRoles);

        // link role
        menu.setLinkPolicy(menuEntity.getLinkPolicy());
        List<Role> linkRoles = menuEntity.getLinkMenuRoleEntities().stream()
                .map(MenuRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        menu.setLinkRoles(linkRoles);

        // return
        return menu;
    }


}
