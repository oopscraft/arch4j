package org.oopscraft.arch4j.core.comment.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.user.repository.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "core_comment")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "target_type")
    private String targetType;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "userId")
    private String userId;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = CommentEntity_.USER_ID, insertable = false, updatable = false)
    private UserEntity user;

}
