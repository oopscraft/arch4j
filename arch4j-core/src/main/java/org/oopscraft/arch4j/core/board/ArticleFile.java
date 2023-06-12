package org.oopscraft.arch4j.core.board;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.board.repository.ArticleFileEntity;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import java.io.InputStream;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleFile extends SystemFieldEntity {

    private String articleId;

    private String fileId;

    private String filename;

    private String contentType;

    @Builder.Default
    private Long length = 0L;

    public static ArticleFile from(ArticleFileEntity articleFileEntity) {
        return ArticleFile.builder()
                .articleId(articleFileEntity.getArticleId())
                .fileId(articleFileEntity.getFileId())
                .filename(articleFileEntity.getFilename())
                .contentType(articleFileEntity.getContentType())
                .length(articleFileEntity.getLength())
                .build();
   }

}
