package org.chomoo.arch4j.web.menu.api.v1.dto;

import lombok.*;
import org.chomoo.arch4j.core.menu.model.Menu;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuResponse {

    private String menuId;

    private String name;

    private String parentMenuId;

    private String link;

    private Menu.Target target;

    private String icon;

    private Integer sort;

    public static MenuResponse from(Menu menu) {
        return MenuResponse.builder()
                .menuId(menu.getMenuId())
                .name(menu.getName())
                .parentMenuId(menu.getParentMenuId())
                .link(menu.getLink())
                .target(menu.getTarget())
                .icon(menu.getIcon())
                .sort(menu.getSort())
                .build();
    }

}
