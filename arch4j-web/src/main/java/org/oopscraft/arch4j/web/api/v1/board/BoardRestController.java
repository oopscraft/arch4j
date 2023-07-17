package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.BoardPermissionEvaluator;
import org.oopscraft.arch4j.core.board.BoardService;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.web.api.v1.board.dto.BoardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "board", description = "Board")
public class BoardRestController {

    private final BoardService boardService;

    private final BoardPermissionEvaluator boardPermissionEvaluator;

    @GetMapping("{boardId}")
    @PreAuthorize("@boardPermissionEvaluator.canAccessBoard(#boardId)")
    @Operation(summary = "get board info", description = "returns board information")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable("boardId") String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        BoardResponse boardResponse = BoardResponse.from(board);

        // set permission
        boardResponse.setCanReadArticle(boardPermissionEvaluator.canReadArticle(board));
        boardResponse.setCanWriteArticle(boardPermissionEvaluator.canWriteArticle(board));
        boardResponse.setCanWriteArticleComment(boardPermissionEvaluator.canWriteArticleComment(board));

        return ResponseEntity.ok(boardResponse);
    }

}
