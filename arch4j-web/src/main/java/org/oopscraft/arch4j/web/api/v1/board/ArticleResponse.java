package org.oopscraft.arch4j.web.api.v1.board;

import lombok.Builder;
import lombok.Data;
import org.oopscraft.arch4j.core.board.Article;

import java.time.LocalDateTime;

@Data
@Builder
public class ArticleResponse {

    private String id;

    private LocalDateTime dateTime;

    private String title;

    private String content;

    private String userId;

    private String userName;

    /**
     * factory method
     * @param article article
     * @return article response
     */
    public static ArticleResponse from(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .dateTime(article.getDateTime())
                .title(article.getTitle())
                .content(article.getContent())
                .userId(article.getUserId())
                .userName(article.getUserName())
                .build();
    }

}
