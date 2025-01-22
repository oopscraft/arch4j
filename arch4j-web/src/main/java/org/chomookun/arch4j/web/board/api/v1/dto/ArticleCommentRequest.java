package org.chomookun.arch4j.web.board.api.v1.dto;

import lombok.*;
import org.chomookun.arch4j.core.board.model.ArticleComment;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleCommentRequest {

    private String articleId;

    private String commentId;

    private String parentCommentId;

    private ArticleComment.ContentFormat contentFormat;

    private String content;

    private String userId;

    private String userName;

    private String password;

}
