package org.oopscraft.arch4j.core.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.repository.ArticleEntity;
import org.oopscraft.arch4j.core.user.repository.UserEntity;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {

    private String articleId;

    private LocalDateTime createdAt;

    @NotBlank(message = "Title is empty")
    private String title;

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
    private Long likeCount = 0L;

    @Builder.Default
    private List<ArticleFile> files = new ArrayList<>();

    /**
     * factory method
     * @param articleEntity article entity
     * @return article info
     */
    public static Article from(ArticleEntity articleEntity) {
        return Article.builder()
                .articleId(articleEntity.getArticleId())
                .createdAt(articleEntity.getCreatedAt())
                .title(articleEntity.getTitle())
                .content(articleEntity.getContent())
                .boardId(articleEntity.getBoardId())
                .userId(articleEntity.getUserId())
                .commentCount(articleEntity.getCommentCount())
                .likeCount(articleEntity.getLikeCount())
                .userName(Optional.ofNullable(articleEntity.getUser())
                        .map(UserEntity::getName)
                        .orElse(articleEntity.getUserName()))
                .userIcon(Optional.ofNullable(articleEntity.getUser())
                        .map(UserEntity::getPhoto)
                        .orElse(null))
                .password(articleEntity.getPassword())
                .build();
    }

}
