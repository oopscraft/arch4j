package org.oopscraft.arch4j.web.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.BoardService;
import org.oopscraft.arch4j.web.security.SecurityUtils;
import org.springframework.stereotype.Component;

@Component("boardPermissionEvaluator")
@RequiredArgsConstructor
public class BoardPermissionEvaluator {

    private final BoardService boardService;

    public boolean hasAccessPermission(Board board) {
        return SecurityUtils.hasPermission(board.getAccessRoles());
    }

    public boolean hasAccessPermission(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return hasAccessPermission(board);
    }

    public boolean hasReadPermission(Board board) {
        return SecurityUtils.hasPermission(board.getReadRoles());
    }

    public boolean hasReadPermission(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return hasReadPermission(board);
    }

    public boolean hasWritePermission(Board board) {
        return SecurityUtils.hasPermission(board.getWriteRoles());
    }

    public boolean hasWritePermission(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return hasWritePermission(board);
    }

    public boolean hasFilePermission(Board board) {
        return SecurityUtils.hasPermission(board.getFileRoles());
    }

    public boolean hasFilePermission(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return hasFilePermission(board);
    }

    public boolean hasCommentPermission(Board board) {
        return SecurityUtils.hasPermission(board.getCommentRoles());
    }

    public boolean hasCommentPermission(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return hasCommentPermission(board);
    }

}
