package org.oopscraft.arch4j.core.page;

import lombok.*;
import org.oopscraft.arch4j.core.page.dao.PagePanelEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PagePanel {

    private String pageId;

    private Integer index;

    private String type;

    private String properties;

    public static PagePanel from(PagePanelEntity pagePanelEntity) {
        return PagePanel.builder()
                .pageId(pagePanelEntity.getPageId())
                .index(pagePanelEntity.getIndex())
                .type(pagePanelEntity.getType())
                .properties(pagePanelEntity.getProperties())
                .build();
    }

}
