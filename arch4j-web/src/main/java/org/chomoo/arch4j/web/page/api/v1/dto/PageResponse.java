package org.chomoo.arch4j.web.page.api.v1.dto;

import lombok.*;
import org.chomoo.arch4j.core.page.model.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse {

    private String pageId;

    private String name;

    private Page.ContentFormat contentFormat;

    private String content;

    /**
     * page factory method
     * @param page page
     * @return page response
     */
    public static PageResponse from(Page page) {
        return PageResponse.builder()
                .pageId(page.getPageId())
                .name(page.getName())
                .contentFormat(page.getContentFormat())
                .content(page.getContent())
                .build();
    }

}
