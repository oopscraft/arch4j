package org.chomookun.arch4j.core.page.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageSearch {

    private String pageId;

    private String name;

}
