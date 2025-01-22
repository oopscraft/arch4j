package org.chomookun.arch4j.web.board.api.v1.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleVoteRequest {

    private String articleId;

    private Long point;

}
