package org.oopscraft.arch4j.web.board.api.v1.dto;

import lombok.Builder;
import lombok.Data;
import org.oopscraft.arch4j.core.board.Article;
import org.oopscraft.arch4j.core.board.ContentFormat;

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

    private ContentFormat contentFormat;

    private String content;

    private String userId;

    private String userName;

    private String userIcon;

    @Builder.Default
    private Long commentCount = 0L;

    @Builder.Default
    private Long votePositiveCount = 0L;

    @Builder.Default
    private Long voteNegativeCount = 0L;

    private boolean canEdit;

    @Builder.Default
    private List<ArticleFileResponse> files = new ArrayList<>();

    public static ArticleResponse from(Article article) {
        return ArticleResponse.builder()
                .articleId(article.getArticleId())
                .createdAt(article.getCreatedAt())
                .title(article.getTitle())
                .contentFormat(article.getContentFormat())
                .content(article.getContent())
                .userId(article.getUserId())
                .userName(article.getUserName())
                .commentCount(article.getCommentCount())
                .votePositiveCount(article.getVotePositiveCount())
                .voteNegativeCount(article.getVoteNegativeCount())
                .files(article.getFiles().stream().map(ArticleFileResponse::from).collect(Collectors.toList()))
                .canEdit(article.canEdit())
                .build();
    }

}
