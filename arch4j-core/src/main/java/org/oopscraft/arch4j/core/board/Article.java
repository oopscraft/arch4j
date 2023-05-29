package org.oopscraft.arch4j.core.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.repository.ArticleEntity;
import org.oopscraft.arch4j.core.comment.Comment;
import org.oopscraft.arch4j.core.user.repository.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {

    private String id;

    private LocalDateTime dateTime;

    private String title;

    private String content;

    private String boardId;

    private String userId;

    private String userName;

    @Builder.Default
    private Long commentCount = 0L;

    @Builder.Default
    private Long likeCount = 0L;

    /**
     * factory method
     * @param articleEntity article entity
     * @return article info
     */
    public static Article from(ArticleEntity articleEntity) {
        return Article.builder()
                .id(articleEntity.getId())
                .dateTime(articleEntity.getDateTime())
                .title(articleEntity.getTitle())
                .content(articleEntity.getContent())
                .boardId(articleEntity.getBoardId())
                .userId(articleEntity.getUserId())
                .commentCount(articleEntity.getCommentCount())
                .likeCount(articleEntity.getLikeCount())
                .userName(Optional.ofNullable(articleEntity.getUser())
                        .map(UserEntity::getName)
                        .orElse(null))
                .build();
    }

}
