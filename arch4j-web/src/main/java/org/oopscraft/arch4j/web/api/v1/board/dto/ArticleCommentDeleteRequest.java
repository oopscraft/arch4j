package org.oopscraft.arch4j.web.api.v1.board.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleCommentDeleteRequest {

    private String password;

}
