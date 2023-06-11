package org.oopscraft.arch4j.web.api.v1.board;

import lombok.Builder;
import lombok.Data;
import org.oopscraft.arch4j.core.board.Article;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ArticleResponse {

    private String articleId;

    private LocalDateTime createdAt;

    private String title;

    private String content;

    private String userId;

    private String userName;

    @Builder.Default
    private Long commentCount = 0L;

    @Builder.Default
    private Long likeCount = 0L;

    @Builder.Default
    private List<org.oopscraft.arch4j.web.api.v1.board.ArticleFileResponse> files = new ArrayList<>();

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
                .build();
    }


    @Data
    @Builder
    static class ArticleFileResponse {
        private String articleId;
        private String id;
        private String name;
        private String length;
    }

}
