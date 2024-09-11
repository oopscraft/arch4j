package org.oopscraft.arch4j.core.page.widget;

import lombok.*;
import org.oopscraft.arch4j.core.page.dao.PageWidgetEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageWidget {

    private String pageId;

    private Integer index;

    private String type;

    private String properties;

    private String url;

    public static PageWidget from(PageWidgetEntity pagePanelEntity) {
        return PageWidget.builder()
                .pageId(pagePanelEntity.getPageId())
                .index(pagePanelEntity.getIndex())
                .type(pagePanelEntity.getType())
                .properties(pagePanelEntity.getProperties())
                .build();
    }

}
