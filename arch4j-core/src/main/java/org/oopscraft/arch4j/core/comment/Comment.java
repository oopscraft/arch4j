package org.oopscraft.arch4j.core.comment;

import lombok.*;
import org.oopscraft.arch4j.core.comment.repository.CommentEntity;
import org.oopscraft.arch4j.core.user.repository.UserEntity;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {

    private String id;

    private String parentId;

    private CommentTarget target;

    private String targetId;

    private String content;

    private String userId;

    private String userName;

    /**
     * factory method
     * @param articleReplyEntity article reply entity
     * @return article reply
     */
    public static Comment from(CommentEntity articleReplyEntity) {
        return Comment.builder()
                .id(articleReplyEntity.getId())
                .parentId(articleReplyEntity.getParentId())
                .target(articleReplyEntity.getTarget())
                .targetId(articleReplyEntity.getTargetId())
                .content(articleReplyEntity.getContent())
                .userId(articleReplyEntity.getUserId())
                .userName(Optional.ofNullable(articleReplyEntity.getUser())
                        .map(UserEntity::getName)
                        .orElse(null))
                .build();
    }

}
