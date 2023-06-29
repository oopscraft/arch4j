package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.oopscraft.arch4j.core.board.ContentFormat;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "Article Request")
public class ArticleRequest {

    @Schema(description = "Article ID")
    private String articleId;

    @Schema(description = "Title")
    private String title;

    @Schema(description = "Content format", defaultValue = "TEXT")
    private ContentFormat contentFormat;

    @Schema(description = "Content")
    private String content;

    @Schema(description = "Anonymous writer name")
    private String userName;

    @Schema(description = "Anonymous article password")
    private String password;

    @Builder.Default
    @Schema(description = "Attachment files")
    private List<ArticleFileRequest> files = new ArrayList<>();

}
