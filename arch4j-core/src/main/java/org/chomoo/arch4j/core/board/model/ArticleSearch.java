package org.chomoo.arch4j.core.board.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleSearch {

    private String title;

    private String content;

    private String boardId;

    private String userId;

}
