package org.oopscraft.arch4j.core.menu;

import lombok.*;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.menu.dao.MenuRoleEntity;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.security.SecurityPolicy;

import java.util.ArrayList;
import java.util.List;
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

}
