package org.oopscraft.arch4j.core.page;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.BaseModel;
import org.oopscraft.arch4j.core.page.dao.PageEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Page extends BaseModel {

    private String pageId;

    private String pageName;

    @Builder.Default
    private ContentFormat contentFormat = ContentFormat.TEXT;

    private String content;

    @Builder.Default
    private List<PageWidget> pageWidgets = new ArrayList<>();

    public static Page from(PageEntity pageEntity) {
        return Page.builder()
                .systemRequired(pageEntity.isSystemRequired())
                .systemUpdatedAt(pageEntity.getSystemUpdatedAt())
                .systemUpdatedBy(pageEntity.getSystemUpdatedBy())
                .pageId(pageEntity.getPageId())
                .pageName(pageEntity.getPageName())
                .contentFormat(pageEntity.getContentFormat())
                .content(pageEntity.getContent())
                .pageWidgets(pageEntity.getPageWidgets().stream()
                        .map(PageWidget::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
