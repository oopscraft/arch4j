package org.oopscraft.arch4j.web.board.api.v1.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleCommentDeleteRequest {

    private String password;

}
