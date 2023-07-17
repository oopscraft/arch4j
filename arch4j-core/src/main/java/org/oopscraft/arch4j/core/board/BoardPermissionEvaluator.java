package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.springframework.stereotype.Component;

@Component("boardPermissionEvaluator")
@RequiredArgsConstructor
public class BoardPermissionEvaluator {

    private final BoardService boardService;

    public boolean hasReadPermission(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return board.hasReadPermission();
    }

    public boolean hasWritePermission(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return board.hasWritePermission();
    }

    public boolean hasCommentPermission(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return board.hasCommentPermission();
    }

}
