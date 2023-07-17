package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.springframework.stereotype.Component;

@Component("boardPermissionEvaluator")
@RequiredArgsConstructor
public class BoardPermissionEvaluator {

    private final BoardService boardService;

    public boolean canAccessBoard(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return canAccessBoard(board);
    }

    public boolean canAccessBoard(Board board) {
        return SecurityUtils.hasPermission(board.getAccessPolicy(), board.getAccessRoles());
    }

    public boolean canReadArticle(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return canReadArticle(board);
    }

    public boolean canReadArticle(Board board) {
        return canAccessBoard(board)
                && SecurityUtils.hasPermission(board.getReadPolicy(), board.getReadRoles());
    }

    public boolean canWriteArticle(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return canWriteArticle(board);
    }

    public boolean canWriteArticle(Board board) {
        return canReadArticle(board)
                && SecurityUtils.hasPermission(board.getWritePolicy(), board.getWriteRoles());
    }

    public boolean canReadArticleComment(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return canReadArticleComment(board);
    }

    public boolean canReadArticleComment(Board board) {
        return canReadArticle(board);       // has read article permission, can read article comment.
    }

    public boolean canWriteArticleComment(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return canWriteArticleComment(board);
    }

    public boolean canWriteArticleComment(Board board) {
        return canWriteArticle(board)
                && SecurityUtils.hasPermission(board.getCommentPolicy(), board.getCommentRoles());
    }

}
