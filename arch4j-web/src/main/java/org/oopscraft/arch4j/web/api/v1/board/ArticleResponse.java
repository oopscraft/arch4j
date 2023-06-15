package org.oopscraft.arch4j.web.api.v1.board;

import lombok.Builder;
import lombok.Data;
import org.oopscraft.arch4j.core.board.Article;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ArticleResponse {

    private String articleId;

    private LocalDateTime createdAt;

    private String title;

    private String content;

    private String userId;

    private String userName;

    private String userIcon;

    @Builder.Default
    private Long commentCount = 0L;

    @Builder.Default
    private Long likeCount = 0L;

    @Builder.Default
    private List<ArticleFileResponse> files = new ArrayList<>();

    /**
     * factory method
     * @param article article
     * @return article response
     */
    public static ArticleResponse from(Article article) {
        return ArticleResponse.builder()
                .articleId(article.getArticleId())
                .createdAt(article.getCreatedAt())
                .title(article.getTitle())
                .content(article.getContent())
                .userId(article.getUserId())
                .userName(article.getUserName())
                .commentCount(article.getCommentCount())
                .likeCount(article.getLikeCount())
                .files(article.getFiles().stream().map(ArticleFileResponse::from).collect(Collectors.toList()))
                .build();
    }

}
