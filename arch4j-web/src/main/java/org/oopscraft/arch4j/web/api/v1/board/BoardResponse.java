package org.oopscraft.arch4j.web.api.v1.board;

import lombok.Builder;
import lombok.Data;
import org.oopscraft.arch4j.core.board.Board;

@Data
@Builder
public class BoardResponse {

    private String id;

    private String name;

    public static BoardResponse from(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .build();
    }

}
