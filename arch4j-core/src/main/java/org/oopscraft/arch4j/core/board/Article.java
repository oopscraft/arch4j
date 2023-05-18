package org.oopscraft.arch4j.core.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.repository.ArticleEntity;
import org.oopscraft.arch4j.core.user.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {

    private String id;

    private String title;

    private String content;

    private String boardId;

    private Board board;

    private String userId;

    private User user;

    @Builder.Default
    private List<ArticleReply> replies = new ArrayList<>();

    /**
     * factory method
     * @param articleEntity article entity
     * @return article info
     */
    public static Article from(ArticleEntity articleEntity) {
        return Article.builder()
                .id(articleEntity.getId())
                .title(articleEntity.getTitle())
                .content(articleEntity.getContent())
                .boardId(articleEntity.getBoardId())
                .userId(articleEntity.getUserId())
                .build();
    }

}
