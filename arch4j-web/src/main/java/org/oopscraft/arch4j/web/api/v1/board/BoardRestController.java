package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.*;
import org.oopscraft.arch4j.core.code.Code;
import org.oopscraft.arch4j.web.api.v1.code.CodeResponse;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.oopscraft.arch4j.web.support.PageableAsQueryParam;
import org.oopscraft.arch4j.web.support.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/board")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;

    private final ArticleService articleService;

    /**
     * returns list of board
     * @param boardSearch search condition
     * @param pageable pagination info
     * @return list of board
     */
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

    /**
     * return board info
     * @param id board id
     * @return board info
     */
    @GetMapping("{id}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable("id") String id) {
        BoardResponse boardResponse = boardService.getBoard(id)
                .map(BoardResponse::from)
                .orElseThrow(() -> new DataNotFoundException(id));
        return ResponseEntity.ok(boardResponse);
    }

    /**
     * returns board article list
     * @param id board id
     * @param pageable pagination info
     * @return article list
     */
    @GetMapping("{id}/article")
    public ResponseEntity<List<ArticleResponse>> getArticles(@PathVariable("id") String id, Pageable pageable) {
        ArticleSearch articleSearch = ArticleSearch.builder()
                .boardId(id)
                .build();
        Page<Article> articlePage = articleService.getArticles(articleSearch, pageable);
        List<ArticleResponse> articleResponses = articlePage.getContent().stream()
                .map(ArticleResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("article", pageable, articlePage.getTotalElements()))
                .body(articleResponses);
    }

}
