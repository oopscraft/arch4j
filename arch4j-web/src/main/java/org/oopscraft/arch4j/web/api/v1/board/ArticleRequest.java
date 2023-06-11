package org.oopscraft.arch4j.web.api.v1.board;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleRequest {

    private String articleId;

    private String title;

    private String content;

    private String userName;

    private String password;

    @Builder.Default
    private List<ArticleFileRequest> files = new ArrayList<>();

}
