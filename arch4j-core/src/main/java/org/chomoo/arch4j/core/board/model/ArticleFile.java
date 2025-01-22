package org.chomoo.arch4j.core.board.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.board.dao.ArticleFileEntity;
import org.chomoo.arch4j.core.common.data.BaseEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleFile extends BaseEntity {

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

   public static List<ArticleFile> from(List<ArticleFileEntity> articleFileEntities) {
        return articleFileEntities.stream()
                .map(ArticleFile::from)
                .collect(Collectors.toList());
   }

}
