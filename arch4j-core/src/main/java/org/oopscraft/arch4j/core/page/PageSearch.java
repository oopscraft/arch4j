package org.oopscraft.arch4j.core.page;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageSearch {

    private String pageId;

    private String name;

}
