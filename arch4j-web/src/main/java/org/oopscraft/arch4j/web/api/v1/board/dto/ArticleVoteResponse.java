package org.oopscraft.arch4j.web.api.v1.board.dto;

import lombok.*;
import org.oopscraft.arch4j.core.board.ArticleVote;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleVoteResponse {

    private String articleId;

    @Builder.Default
    private Long point = 0L;

    @Builder.Default
    private Long positiveCount = 0L;

    @Builder.Default
    private Long negativeCount = 0L;

    public static ArticleVoteResponse from(ArticleVote articleVote) {
        return ArticleVoteResponse.builder()
                .articleId(articleVote.getArticleId())
                .point(articleVote.getPoint())
                .build();
    }

}
