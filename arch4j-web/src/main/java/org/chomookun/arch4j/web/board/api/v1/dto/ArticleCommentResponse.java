package org.chomookun.arch4j.web.board.api.v1.dto;

import lombok.*;
import org.chomookun.arch4j.core.board.model.ArticleComment;
import org.chomookun.arch4j.web.security.support.SecurityUtils;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleCommentResponse {

    private String articleId;

    private String commentId;

    private String parentCommentId;

    private LocalDateTime createdAt;

    private ArticleComment.ContentFormat contentFormat;

    private String content;

    private String userId;

    private String userName;

    private String userIcon;

    @Builder.Default
    private Long likeCount = 0L;

    private boolean canEdit;

    public static ArticleCommentResponse from(ArticleComment articleComment) {
        return ArticleCommentResponse.builder()
                .articleId(articleComment.getArticleId())
                .commentId(articleComment.getCommentId())
                .parentCommentId(articleComment.getParentCommentId())
                .createdAt(articleComment.getCreatedAt())
                .contentFormat(articleComment.getContentFormat())
                .content(articleComment.getContent())
                .userId(articleComment.getUserId())
                .userName(articleComment.getUserName())
                .likeCount(articleComment.getLikeCount())
                .canEdit(articleComment.getUserId() == null || articleComment.getUserId().equals(SecurityUtils.getCurrentUserId()))
                .build();
    }

}
