package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.*;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
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
     * @param boardId board id
     * @return board info
     */
    @GetMapping("{boardId}")
    @Operation(summary = "get board info", description = "returns board information")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable("boardId") String boardId) {
        BoardResponse boardResponse = boardService.getBoard(boardId)
                .map(BoardResponse::from)
                .orElseThrow(() -> new DataNotFoundException(boardId));
        return ResponseEntity.ok(boardResponse);
    }

    /**
     * returns board article list
     * @param boardId board id
     * @param pageable pagination info
     * @return article list
     */
    @GetMapping("{boardId}/article")
    @Operation(summary = "get list of articles")
    public ResponseEntity<List<ArticleResponse>> getArticles(@PathVariable("boardId") String boardId, Pageable pageable) {
        ArticleSearch articleSearch = ArticleSearch.builder()
                .boardId(boardId)
                .build();
        Page<Article> articlePage = articleService.getArticles(articleSearch, pageable);
        List<ArticleResponse> articleResponses = articlePage.getContent().stream()
                .map(ArticleResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("article", pageable, articlePage.getTotalElements()))
                .body(articleResponses);
    }

    /**
     * save article
     * @param boardId board id
     * @param articleRequest article info
     */
    @PostMapping("{boardId}/article")
    @Operation(summary = "save article info")
    public ResponseEntity<Void> saveArticle(@PathVariable("boardId") String boardId, @RequestBody ArticleRequest articleRequest) {
        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .boardId(boardId)
                .build();
        articleService.saveArticle(article);
        return ResponseEntity.ok().build();
    }

}
