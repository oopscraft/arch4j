package org.chomoo.arch4j.core.board.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardSearch {

    private String boardId;

    private String name;

}
