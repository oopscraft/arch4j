package org.oopscraft.arch4j.web.api.v1.comment;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequest {

    private String articleId;

    private String id;

    private String parentId;

    private String comment;

    private String userId;

    private String userName;

}
