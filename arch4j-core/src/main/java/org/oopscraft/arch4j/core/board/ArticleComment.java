package org.oopscraft.arch4j.core.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.repository.ArticleCommentEntity;
import org.oopscraft.arch4j.core.user.repository.UserEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleComment {

    private String articleId;

    private String commentId;

    private String parentCommentId;

    private LocalDateTime createdAt;

    private String content;

    private String userId;

    private String userName;

    private String password;

    private Long likeCount;

    public static ArticleComment from(ArticleCommentEntity articleCommentEntity) {
        return ArticleComment.builder()
                .articleId(articleCommentEntity.getArticleId())
                .commentId(articleCommentEntity.getCommentId())
                .createdAt(articleCommentEntity.getCreatedAt())
                .parentCommentId(articleCommentEntity.getParentCommentId())
                .content(articleCommentEntity.getContent())
                .userId(articleCommentEntity.getUserId())
                .userName(Optional.ofNullable(articleCommentEntity.getUser())
                        .map(UserEntity::getName)
                        .orElse(articleCommentEntity.getUserName()))
                .password(articleCommentEntity.getPassword())
                .likeCount(articleCommentEntity.getLikeCount())
                .build();
    }

}
