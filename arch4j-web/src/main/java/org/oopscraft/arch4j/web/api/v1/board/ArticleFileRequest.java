package org.oopscraft.arch4j.web.api.v1.board;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleFileRequest {

    private String articleId;

    private String fileId;

    private String filename;

    private String contentType;

    @Builder.Default
    private Long length = 0L;

}
