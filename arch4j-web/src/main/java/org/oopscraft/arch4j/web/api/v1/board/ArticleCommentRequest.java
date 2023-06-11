package org.oopscraft.arch4j.web.api.v1.board;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleCommentRequest {

    private String articleId;

    private String commentId;

    private String parentCommentId;

    private String content;

    private String userId;

    private String userName;

    private String password;

}
