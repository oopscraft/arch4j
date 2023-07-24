package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.*;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.web.api.v1.board.dto.ArticleCommentDeleteRequest;
import org.oopscraft.arch4j.web.api.v1.board.dto.ArticleCommentRequest;
import org.oopscraft.arch4j.web.api.v1.board.dto.ArticleCommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/board/{boardId}/article/{articleId}/comment")
@RequiredArgsConstructor
@Tag(name = "board")
public class ArticleCommentRestController {

    private final BoardService boardService;

    private final ArticleCommentService articleCommentService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @Operation(summary = "get article comments")
    public ResponseEntity<List<ArticleCommentResponse>> getArticleComments(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId") String articleId
    ) {
        // get board
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check permission
        SecurityUtils.checkPermission(board.getReadPolicy(), board.getReadRoles());

        // return article responses
        List<ArticleCommentResponse> commentResponses = articleCommentService.getArticleComments(articleId).stream()
                .map(ArticleCommentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentResponses);
    }

    @PostMapping
    @Transactional
    @PreAuthorize("@boardPermissionEvaluator.hasCommentPermission(#boardId)")
    @Operation(summary = "creates article comment")
    public ResponseEntity<ArticleComment> createArticleComment(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId") String articleId,
            @Parameter(description = "article comment request", schema = @Schema(type = "object", implementation = ArticleCommentRequest.class))
            @RequestBody ArticleCommentRequest articleCommentRequest
    ) {
        // get board
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check permission
        SecurityUtils.checkPermission(board.getCommentPolicy(), board.getCommentRoles());

        // check anonymous user
        if(!SecurityUtils.isAuthenticated()) {
            if(articleCommentRequest.getUserName() == null) {
                throw new RuntimeException("userName required");
            }
            if(articleCommentRequest.getPassword() == null) {
                throw new RuntimeException("password required");
            }
        }

        // create article comment
        ArticleComment articleComment = ArticleComment.builder()
                .articleId(articleId)
                .commentId(articleCommentRequest.getCommentId())
                .parentCommentId(articleCommentRequest.getParentCommentId())
                .contentFormat(articleCommentRequest.getContentFormat())
                .content(articleCommentRequest.getContent())
                .userId(SecurityUtils.getCurrentUserId())
                .userName(articleCommentRequest.getUserName())
                .password(articleCommentRequest.getPassword())
                .build();

        // authenticated user
        if(SecurityUtils.isAuthenticated()) {
            articleComment.setUserId(SecurityUtils.getCurrentUserId());
        }else{
            articleComment.setUserId(null);
            articleComment.setUserName(articleCommentRequest.getUserName());
            articleComment.setPassword(articleCommentRequest.getPassword());
        }

        // save article comment
        articleComment = articleCommentService.saveArticleComment(articleComment);
        return ResponseEntity.ok(articleComment);
    }

    @GetMapping("{commentId}")
    @Operation(summary = "get article comment")
    public ResponseEntity<ArticleCommentResponse> getArticleComment(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description =  "article ID")
            @PathVariable("articleId") String articleId,
            @Parameter(description = "comment ID")
            @PathVariable("commentId") String commentId
    ) {
        // get board
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check permission
        SecurityUtils.checkPermission(board.getReadPolicy(), board.getReadRoles());

        // return article comment
        ArticleCommentResponse commentResponse = articleCommentService.getArticleComment(articleId, commentId)
                .map(ArticleCommentResponse::from)
                .orElseThrow();
        return ResponseEntity.ok(commentResponse);
    }

    @PutMapping("{commentId}")
    @Transactional
    @PreAuthorize("@boardPermissionEvaluator.hasCommentPermission(#boardId)")
    @Operation(summary = "edit article comment")
    public ResponseEntity<ArticleCommentResponse> modifyArticleComment(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @PathVariable("commentId") String commentId,
            @RequestBody ArticleCommentRequest articleCommentRequest
    ) {
        // get board
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check permission
        SecurityUtils.checkPermission(board.getCommentPolicy(), board.getCommentRoles());

        // get target article comment
        ArticleComment articleComment = articleCommentService.getArticleComment(articleId, commentId).orElseThrow();

        // writer is anonymous user
        if(articleComment.getUserId() == null) {
            if(!passwordEncoder.matches(articleCommentRequest.getPassword(), articleComment.getPassword())) {
                throw new RuntimeException("password not matches");
            }
        }
        // writer is authenticated user
        else {
            if(!Objects.equals(SecurityUtils.getCurrentUserId(), articleComment.getUserId())) {
                throw new RuntimeException("user is not writer");
            }
        }

        // change article comment
        articleComment.setUserName(articleCommentRequest.getUserName());
        articleComment.setContentFormat(articleCommentRequest.getContentFormat());
        articleComment.setContent(articleCommentRequest.getContent());

        // save
        articleComment = articleCommentService.saveArticleComment(articleComment);
        return ResponseEntity.ok(ArticleCommentResponse.from(articleComment));
    }

    @DeleteMapping("{commentId}")
    @Transactional
    @PreAuthorize("@boardPermissionEvaluator.hasCommentPermission(#boardId)")
    @Operation(summary = "delete article comment")
    public ResponseEntity<Void> deleteArticleComment(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId") String articleId,
            @Parameter(description = "comment ID")
            @PathVariable("commentId") String commentId,
            @Parameter(description = "article delete request", schema = @Schema(type = "object", implementation = ArticleCommentDeleteRequest.class))
            @RequestBody ArticleCommentDeleteRequest articleCommentDeleteRequest
    ) {
        // get board
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check permission
        SecurityUtils.checkPermission(board.getCommentPolicy(), board.getCommentRoles());

        // get target article comment to delete
        ArticleComment articleComment = articleCommentService.getArticleComment(articleId, commentId).orElseThrow();

        // writer is anonymous user
        if(articleComment.getUserId() == null) {
            if(!passwordEncoder.matches(articleCommentDeleteRequest.getPassword(), articleComment.getPassword())) {
                throw new RuntimeException("password not match");
            }
        }
        // writer is authenticated user
        else{
            if(!Objects.equals(articleComment.getUserId(),SecurityUtils.getCurrentUserId())) {
                throw new RuntimeException("not writer");
            }
        }

        // delete article commit
        articleCommentService.deleteArticleComment(articleId, commentId);
        return ResponseEntity.ok().build();
    }

}
