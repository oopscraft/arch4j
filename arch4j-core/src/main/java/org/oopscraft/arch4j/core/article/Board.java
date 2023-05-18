package org.oopscraft.arch4j.core.article;

import lombok.*;
import org.oopscraft.arch4j.core.article.repository.BoardEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Board {

    private String id;

    private String name;

    /**
     * factory method
     * @param boardEntity board entity
     * @return board info
     */
    public static Board from(BoardEntity boardEntity) {
        return Board.builder()
                .id(boardEntity.getId())
                .name(boardEntity.getName())
                .build();
    }

}
