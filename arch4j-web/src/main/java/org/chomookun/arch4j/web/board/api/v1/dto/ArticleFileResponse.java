package org.chomookun.arch4j.web.board.api.v1.dto;

import lombok.*;
import org.chomookun.arch4j.core.board.model.ArticleFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleFileResponse {

    private String articleId;

    private String fileId;

    private String filename;

    private String contentType;

    @Builder.Default
    private Long length = 0L;

    public static ArticleFileResponse from(ArticleFile articleFile) {
        return ArticleFileResponse.builder()
                .articleId(articleFile.getArticleId())
                .fileId(articleFile.getFileId())
                .filename(articleFile.getFilename())
                .contentType(articleFile.getContentType())
                .length(articleFile.getLength())
                .build();
    }

}
