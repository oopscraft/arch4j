package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.*;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.web.api.v1.board.dto.ArticleVoteRequest;
import org.oopscraft.arch4j.web.api.v1.board.dto.ArticleVoteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("api/v1/boards/{boardId}/articles/{articleId}/vote")
@RequiredArgsConstructor
@Tag(name = "board")
public class ArticleVoteRestController {

    private final ArticleVoteService articleVoteService;

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<ArticleVoteResponse> getArticleVote(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId
    ) {
        // get board info
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check permission
        SecurityUtils.checkPermission(board.getAccessPolicy(), board.getAccessRoles());
        SecurityUtils.checkPermission(board.getReadPolicy(), board.getReadRoles());

        // get article vote
        String userId = SecurityUtils.getCurrentUserId();
        ArticleVoteResponse articleVoteResponse = articleVoteService.getArticleVote(articleId, userId)
                .map(ArticleVoteResponse::from)
                .orElse(ArticleVoteResponse.builder()
                        .articleId(articleId)
                        .point(0L)
                        .build());

        // calculate count
        AtomicReference<Long> positiveCount = new AtomicReference<>(0L);
        AtomicReference<Long> negativeCount = new AtomicReference<>(0L);
        articleVoteService.getArticleVotes(articleId).forEach(articleVote -> {
            if(articleVote.getPoint() > 0) {
                positiveCount.getAndSet(positiveCount.get() + 1);
            }
            if(articleVote.getPoint() < 0) {
                negativeCount.getAndSet(negativeCount.get() + 1);
            }
        });
        articleVoteResponse.setPositiveCount(positiveCount.get());
        articleVoteResponse.setNegativeCount(negativeCount.get());

        // response
        return ResponseEntity.ok()
                .body(articleVoteResponse);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> saveArticleVote(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @RequestBody ArticleVoteRequest articleVoteRequest
    ) {
        // check authenticated
        SecurityUtils.checkAuthenticated();

        // get board info
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check permission
        SecurityUtils.checkPermission(board.getAccessPolicy(), board.getAccessRoles());
        SecurityUtils.checkPermission(board.getReadPolicy(), board.getReadRoles());

        // save
        ArticleVote articleVote = ArticleVote.builder()
                .articleId(articleId)
                .userId(SecurityUtils.getCurrentUserId())
                .point(articleVoteRequest.getPoint())
                .build();
        articleVoteService.saveArticleVote(articleVote);

        // response
        return ResponseEntity.ok().build();
    }

}
