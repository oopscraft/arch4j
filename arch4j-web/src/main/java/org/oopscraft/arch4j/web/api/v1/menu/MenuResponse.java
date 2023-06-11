package org.oopscraft.arch4j.web.api.v1.menu;

import lombok.*;
import org.oopscraft.arch4j.core.menu.Menu;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuResponse {

    private String id;

    private String parentId;

    private String name;

    private String link;

    private String target;

    private String icon;

    private Integer sort;

    /**
     * factory method
     * @param menu menu
     * @return menu response
     */
    public static MenuResponse from(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getMenuId())
                .parentId(menu.getParentMenuId())
                .name(menu.getName())
                .link(menu.getLink())
                .target(menu.getTarget())
                .icon(menu.getIcon())
                .sort(menu.getSort())
                .build();

    }

}
