package org.oopscraft.arch4j.core.menu;

import lombok.*;
import org.oopscraft.arch4j.core.menu.repository.MenuEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    private String id;

    private String parentId;

    private String name;

    private String link;

    private String target;

    private Integer sort;

    /**
     * factory method from menu entity
     * @param menuEntity menu entity
     * @return menu
     */
    public static Menu from(MenuEntity menuEntity) {
        return Menu.builder()
                .id(menuEntity.getId())
                .parentId(menuEntity.getParentId())
                .name(menuEntity.getName())
                .link(menuEntity.getLink())
                .target(menuEntity.getTarget())
                .sort(menuEntity.getSort())
                .build();
    }

}
