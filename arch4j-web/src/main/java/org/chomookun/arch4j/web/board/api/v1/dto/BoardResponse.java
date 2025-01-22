package org.chomookun.arch4j.web.board.api.v1.dto;

import lombok.*;
import org.chomookun.arch4j.core.board.model.Board;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardResponse {

    private String boardId;

    private String name;

    private String icon;

    private Board.MessageFormat messageFormat;

    private String message;

    private String skin;

    private Integer pageSize;

    private boolean fileEnabled;

    private boolean commentEnabled;

    private boolean hasAccessPermission;

    private boolean hasReadPermission;

    private boolean hasWritePermission;

    private boolean hasFilePermission;

    private boolean hasCommentPermission;

    /**
     * board response factory method
     * @param board board
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
                .commentEnabled(board.isCommentEnabled())
                .build();
    }

}
