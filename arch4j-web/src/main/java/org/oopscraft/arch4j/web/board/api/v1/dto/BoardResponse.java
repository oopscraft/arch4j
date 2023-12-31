package org.oopscraft.arch4j.web.board.api.v1.dto;

import lombok.*;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.MessageFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardResponse {

    private String boardId;

    private String boardName;

    private String icon;

    private MessageFormat messageFormat;

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

    public static BoardResponse from(Board board) {
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .boardName(board.getBoardName())
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
