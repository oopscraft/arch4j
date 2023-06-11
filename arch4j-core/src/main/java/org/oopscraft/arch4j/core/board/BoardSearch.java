package org.oopscraft.arch4j.core.board;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardSearch {

    private String boardId;

    private String name;

}
