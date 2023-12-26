package org.oopscraft.arch4j.core.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.dao.ArticleEntity;
import org.oopscraft.arch4j.core.user.dao.UserEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
                        .map(UserEntity::getUserName)
                        .orElse(articleEntity.getUserName()))
                .userIcon(Optional.ofNullable(articleEntity.getUser())
                        .map(UserEntity::getPhoto)
                        .orElse(null))
                .password(articleEntity.getPassword())
                .build();
    }

    public static List<Article> from(List<ArticleEntity> articleEntities) {
        return articleEntities.stream()
                .map(Article::from)
                .collect(Collectors.toList());
    }

}
