package org.oopscraft.arch4j.web.api.v1.comment;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequest {

    private String id;

    private String parentId;

    private String content;

    private String userId;

    private String userName;

}
