package org.oopscraft.arch4j.core.comment;

import lombok.*;
import org.oopscraft.arch4j.core.comment.repository.CommentEntity;
import org.oopscraft.arch4j.core.user.repository.UserEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {

    private String id;

    private String ownerType;

    private String ownerId;

    private LocalDateTime createdAt;

    private String parentId;

    private String content;

    private String userId;

    private String userName;

    private String password;

    private Long likeCount;

    /**
     * factory method
     * @param commentEntity article reply entity
     * @return article reply
     */
    public static Comment from(CommentEntity commentEntity) {
        return Comment.builder()
                .id(commentEntity.getId())
                .ownerType(commentEntity.getOwnerType())
                .ownerId(commentEntity.getOwnerId())
                .createdAt(commentEntity.getCreatedAt())
                .parentId(commentEntity.getParentId())
                .content(commentEntity.getContent())
                .userId(commentEntity.getUserId())
                .userName(Optional.ofNullable(commentEntity.getUser())
                        .map(UserEntity::getName)
                        .orElse(commentEntity.getUserName()))
                .password(commentEntity.getPassword())
                .likeCount(commentEntity.getLikeCount())
                .build();
    }

}
