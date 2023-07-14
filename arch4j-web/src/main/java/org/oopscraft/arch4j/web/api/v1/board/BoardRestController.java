package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.*;
import org.oopscraft.arch4j.web.api.v1.board.dto.BoardResponse;
import org.oopscraft.arch4j.web.support.PageableAsQueryParam;
import org.oopscraft.arch4j.web.support.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "board", description = "Board")
public class BoardRestController {

    private final BoardService boardService;

    @GetMapping
    @Operation(summary = "get list of board", description = "returns board list")
    @PageableAsQueryParam
    public ResponseEntity<List<BoardResponse>> getBoards(BoardSearch boardSearch, Pageable pageable) {
        Page<Board> boardPage = boardService.getBoards(boardSearch, pageable);
        List<BoardResponse> boardResponses = boardPage.getContent().stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("board", pageable, boardPage.getTotalElements()))
                .body(boardResponses);
    }

    @GetMapping("{boardId}")
    @Operation(summary = "get board info", description = "returns board information")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable("boardId") String boardId) {
        BoardResponse boardResponse = boardService.getBoard(boardId)
                .map(BoardResponse::from)
                .orElseThrow();
        return ResponseEntity.ok(boardResponse);
    }

}
