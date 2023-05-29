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

    private TargetType targetType;

    private String targetId;

    private String parentId;

    private String content;

    private String userId;

    private String userName;

    /**
     * factory method
     * @param commentEntity article reply entity
     * @return article reply
     */
    public static Comment from(CommentEntity commentEntity) {
        return Comment.builder()
                .id(commentEntity.getId())
                .parentId(commentEntity.getParentId())
                .targetType(commentEntity.getTargetType())
                .targetId(commentEntity.getTargetId())
                .content(commentEntity.getContent())
                .userId(commentEntity.getUserId())
                .userName(Optional.ofNullable(commentEntity.getUser())
                        .map(UserEntity::getName)
                        .orElse(null))
                .build();
    }

}
