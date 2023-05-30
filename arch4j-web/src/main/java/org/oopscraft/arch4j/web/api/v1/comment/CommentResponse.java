package org.oopscraft.arch4j.web.api.v1.comment;

import lombok.*;
import org.oopscraft.arch4j.core.comment.Comment;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {

    private String id;

    private LocalDateTime createdAt;

    private String parentId;

    private String content;

    private String userId;

    private String userName;

    @Builder.Default
    private Long likeCount = 0L;

    /**
     * factory method
     * @param comment comment
     * @return comment response
     */
    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .parentId(comment.getParentId())
                .content(comment.getContent())
                .userId(comment.getUserId())
                .userName(comment.getUserName())
                .likeCount(comment.getLikeCount())
                .build();
    }

}
