package org.oopscraft.arch4j.core.board;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.board.repository.ArticleFileEntity;
import org.oopscraft.arch4j.core.board.repository.ArticleVoteEntity;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleVote {

    private String articleId;

    private String userId;

    @Builder.Default
    private Long point = 0L;

    public static ArticleVote from(ArticleVoteEntity articleVoteEntity) {
        return ArticleVote.builder()
                .articleId(articleVoteEntity.getArticleId())
                .userId(articleVoteEntity.getUserId())
                .point(articleVoteEntity.getPoint())
                .build();
   }

}
