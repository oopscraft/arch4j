package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.oopscraft.arch4j.core.board.TextFormat;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "Article delete request")
public class ArticleDeleteRequest {

    @Schema(description = "Anonymous article password")
    private String password;

}
