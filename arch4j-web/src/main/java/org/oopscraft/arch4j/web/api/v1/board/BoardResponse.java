package org.oopscraft.arch4j.web.api.v1.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.TextFormat;
import org.oopscraft.arch4j.core.security.SecurityPolicy;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardResponse {

    private String boardId;

    private String name;

    private String icon;

    private TextFormat messageFormat;

    private String message;

    private String skin;

    private Integer pageSize;

    private boolean fileEnabled;

    private SecurityPolicy accessPolicy;

    private SecurityPolicy readPolicy;

    private SecurityPolicy writePolicy;

    private boolean commentEnabled;

    private SecurityPolicy commentPolicy;

    /**
     * factory method
     * @param board board info
     * @return board response
     */
    public static BoardResponse from(Board board) {
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .name(board.getName())
                .icon(board.getIcon())
                .messageFormat(board.getMessageFormat())
                .message(board.getMessage())
                .skin(board.getSkin())
                .pageSize(board.getPageSize())
                .fileEnabled(board.isFileEnabled())
                .accessPolicy(board.getAccessPolicy())
                .readPolicy(board.getReadPolicy())
                .writePolicy(board.getWritePolicy())
                .commentEnabled(board.isCommentEnabled())
                .commentPolicy(board.getCommentPolicy())
                .build();
    }

}
