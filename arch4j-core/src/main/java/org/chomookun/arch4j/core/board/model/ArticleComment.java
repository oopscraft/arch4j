package org.chomookun.arch4j.core.board.model;

import lombok.*;
import org.chomookun.arch4j.core.board.dao.ArticleCommentEntity;
import org.chomookun.arch4j.core.security.dao.UserEntity;

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

    private ContentFormat contentFormat;

    private String content;

    private String userId;

    private String userName;

    private String userIcon;

    private String password;

    private Long likeCount;

    public enum ContentFormat { TEXT, MARKDOWN }

    public static ArticleComment from(ArticleCommentEntity articleCommentEntity) {
        return ArticleComment.builder()
                .articleId(articleCommentEntity.getArticleId())
                .commentId(articleCommentEntity.getCommentId())
                .createdAt(articleCommentEntity.getCreatedAt())
                .parentCommentId(articleCommentEntity.getParentCommentId())
                .contentFormat(articleCommentEntity.getContentFormat())
                .content(articleCommentEntity.getContent())
                .userId(articleCommentEntity.getUserId())
                .userName(Optional.ofNullable(articleCommentEntity.getUser())
                        .map(UserEntity::getUsername)
                        .orElse(articleCommentEntity.getUserName()))
                .userIcon(Optional.ofNullable(articleCommentEntity.getUser())
                        .map(UserEntity::getPhoto)
                        .orElse(null))
                .password(articleCommentEntity.getPassword())
                .likeCount(articleCommentEntity.getLikeCount())
                .build();
    }

}
