package org.oopscraft.arch4j.core.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.dao.ArticleCommentEntity;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.core.user.dao.UserEntity;

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

    public boolean canEdit() {
        return userId == null || userId.equals(SecurityUtils.getCurrentUserId());
    }

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
                        .map(UserEntity::getUserName)
                        .orElse(articleCommentEntity.getUserName()))
                .userIcon(Optional.ofNullable(articleCommentEntity.getUser())
                        .map(UserEntity::getPhoto)
                        .orElse(null))
                .password(articleCommentEntity.getPassword())
                .likeCount(articleCommentEntity.getLikeCount())
                .build();
    }

}
