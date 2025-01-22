package org.chomoo.arch4j.core.board.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.board.model.Article;
import org.chomoo.arch4j.core.common.data.BaseEntity;
import org.chomoo.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.chomoo.arch4j.core.security.dao.UserEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "core_article",
    indexes = {
        @Index(name = "ix_board_id_created_at", columnList = "board_id, created_at"),
        @Index(name = "ix_title", columnList = "title"),
        @Index(name = "ix_user_id", columnList = "user_id")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleEntity extends BaseEntity {

    @Id
    @Column(name = "article_id", length = 32)
    private String articleId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "content_format", length = 16)
    private Article.ContentFormat contentFormat;

    @NotBlank
    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "board_id", length = 32)
    private String boardId;

    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "comment_count")
    @Builder.Default
    private Long commentCount = 0L;

    @Column(name = "vote_positive_count")
    @Builder.Default
    private Long votePositiveCount = 0L;

    @Column(name = "vote_negative_count")
    @Builder.Default
    private Long voteNegativeCount = 0L;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @Converter(autoApply = true)
    public static class ContentFormatConverter extends AbstractEnumConverter<Article.ContentFormat> { }

}
