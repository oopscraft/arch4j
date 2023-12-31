package org.oopscraft.arch4j.web.menu.api.v1.dto;

import lombok.*;
import org.oopscraft.arch4j.core.menu.Menu;
import org.oopscraft.arch4j.core.menu.MenuTarget;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuResponse {

    private String menuId;

    private String menuName;

    private String parentMenuId;

    private String link;

    private MenuTarget target;

    private String icon;

    private Integer sort;

    public static MenuResponse from(Menu menu) {
        return MenuResponse.builder()
                .menuId(menu.getMenuId())
                .menuName(menu.getMenuName())
                .parentMenuId(menu.getParentMenuId())
                .link(menu.getLink())
                .target(menu.getTarget())
                .icon(menu.getIcon())
                .sort(menu.getSort())
                .build();
    }

}
