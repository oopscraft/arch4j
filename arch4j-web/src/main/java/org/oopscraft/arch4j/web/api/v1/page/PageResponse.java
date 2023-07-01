package org.oopscraft.arch4j.web.api.v1.page;

import lombok.*;
import org.oopscraft.arch4j.core.page.ContentFormat;
import org.oopscraft.arch4j.core.page.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse {

    private String pageId;

    private String name;

    private ContentFormat contentFormat;

    private String content;

    public static PageResponse from(Page page) {
        return PageResponse.builder()
                .pageId(page.getPageId())
                .name(page.getName())
                .contentFormat(page.getContentFormat())
                .content(page.getContent())
                .build();
    }

}
