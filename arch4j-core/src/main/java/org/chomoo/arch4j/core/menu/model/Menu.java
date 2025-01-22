package org.chomoo.arch4j.core.menu.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.common.data.BaseModel;
import org.chomoo.arch4j.core.menu.dao.MenuEntity;
import org.chomoo.arch4j.core.menu.dao.MenuRoleEntity;
import org.chomoo.arch4j.core.security.model.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Menu extends BaseModel {

    private String menuId;

    private String name;

    private String parentMenuId;

    private String link;

    private Target target;

    private String icon;

    private Integer sort;

    private String note;

    @Builder.Default
    private List<Role> viewRoles = new ArrayList<>();

    @Builder.Default
    private List<Role> linkRoles = new ArrayList<>();

    public enum Target { _self, _blank }

    /**
     * menu factory method
     * @param menuEntity menu entity
     * @return menu
     */
    public static Menu from(MenuEntity menuEntity) {
        Menu menu = Menu.builder()
                .systemRequired(menuEntity.isSystemRequired())
                .systemUpdatedAt(menuEntity.getSystemUpdatedAt())
                .systemUpdatedBy(menuEntity.getSystemUpdatedBy())
                .menuId(menuEntity.getMenuId())
                .name(menuEntity.getName())
                .parentMenuId(menuEntity.getParentMenuId())
                .link(menuEntity.getLink())
                .target(menuEntity.getTarget())
                .icon(menuEntity.getIcon())
                .sort(menuEntity.getSort())
                .note(menuEntity.getNote())
                .build();

        // view role
        List<Role> viewRoles = menuEntity.getViewMenuRoles().stream()
                .map(MenuRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        menu.setViewRoles(viewRoles);

        // link role
        List<Role> linkRoles = menuEntity.getLinkMenuRoles().stream()
                .map(MenuRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        menu.setLinkRoles(linkRoles);

        // return
        return menu;
    }

}
