package org.chomookun.arch4j.core.board.model;

import lombok.*;
import org.chomookun.arch4j.core.board.dao.ArticleEntity;
import org.chomookun.arch4j.core.security.dao.UserEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {

    private String articleId;

    private LocalDateTime createdAt;

    @NotBlank(message = "Title is empty")
    private String title;

    @Builder.Default
    @NotNull(message = "Content format is empty")
    private ContentFormat contentFormat = ContentFormat.TEXT;

    @NotBlank(message = "Content is empty")
    private String content;

    @NotBlank(message = "Board ID is empty")
    private String boardId;

    private String userId;

    private String userName;

    private String userIcon;

    private String password;

    @Builder.Default
    private Long commentCount = 0L;

    @Builder.Default
    private Long votePositiveCount = 0L;

    @Builder.Default
    private Long voteNegativeCount = 0L;

    @Builder.Default
    private List<ArticleFile> files = new ArrayList<>();

    public enum ContentFormat { TEXT, MARKDOWN }

    /**
     * article factory method
     * @param articleEntity article entity
     * @return article
     */
    public static Article from(ArticleEntity articleEntity) {
        return Article.builder()
                .articleId(articleEntity.getArticleId())
                .createdAt(articleEntity.getCreatedAt())
                .title(articleEntity.getTitle())
                .contentFormat(articleEntity.getContentFormat())
                .content(articleEntity.getContent())
                .boardId(articleEntity.getBoardId())
                .userId(articleEntity.getUserId())
                .commentCount(articleEntity.getCommentCount())
                .votePositiveCount(articleEntity.getVotePositiveCount())
                .voteNegativeCount(articleEntity.getVoteNegativeCount())
                .userName(Optional.ofNullable(articleEntity.getUser())
                        .map(UserEntity::getUsername)
                        .orElse(articleEntity.getUserName()))
                .userIcon(Optional.ofNullable(articleEntity.getUser())
                        .map(UserEntity::getPhoto)
                        .orElse(null))
                .password(articleEntity.getPassword())
                .build();
    }

    /**
     * articles factory method
     * @param articleEntities article entities
     * @return articles
     */
    public static List<Article> from(List<ArticleEntity> articleEntities) {
        return articleEntities.stream()
                .map(Article::from)
                .collect(Collectors.toList());
    }

}
