package org.oopscraft.arch4j.core.article;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardSearch {

    private String id;

    private String name;

}
