package org.chomoo.arch4j.core.board.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.board.model.ArticleComment;
import org.chomoo.arch4j.core.common.data.BaseEntity;
import org.chomoo.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.chomoo.arch4j.core.security.dao.UserEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "core_article_comment")
@IdClass(ArticleCommentEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleCommentEntity extends BaseEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {
        private String articleId;
        private String commentId;
    }

    @Id
    @Column(name = "article_id", length = 32)
    private String articleId;

    @Id
    @Column(name = "comment_id", length = 32)
    private String commentId;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "parent_comment_id", length = 32)
    private String parentCommentId;

    @NotNull
    @Column(name = "content_format", length = 16)
    private ArticleComment.ContentFormat contentFormat;

    @NotBlank
    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "user_id", length = 16)
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "like_count")
    @Builder.Default
    private Long likeCount = 0L;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @Converter(autoApply = true)
    public static class ContentFormatConverter extends AbstractEnumConverter<ArticleComment.ContentFormat> { }

}
