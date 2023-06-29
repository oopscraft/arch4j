package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "Article delete request")
public class ArticleDeleteRequest {

    @Schema(description = "Anonymous article password")
    private String password;

}
