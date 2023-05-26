package org.oopscraft.arch4j.web.api.v1.board;

import lombok.Builder;
import lombok.Data;
import org.oopscraft.arch4j.core.board.Article;

@Data
@Builder
public class ArticleResponse {

    private String id;

    private String title;

    private String content;

    private String userName;

    private String dateTime;

    public static ArticleResponse from(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }

}
