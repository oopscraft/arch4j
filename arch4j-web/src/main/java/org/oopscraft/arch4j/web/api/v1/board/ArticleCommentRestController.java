package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.*;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/board/{boardId}/article/{articleId}/comment")
@RequiredArgsConstructor
public class ArticleCommentRestController {

    private final ArticleCommentService articleCommentService;

    private final PasswordEncoder passwordEncoder;

    /**
     * return comment list
     * @param boardId board id
     * @param articleId article id
     * @return comment list
     */
    @GetMapping
    @Operation(summary = "get article replies")
    public ResponseEntity<List<ArticleCommentResponse>> getArticleComments(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId
    ) {
        List<ArticleCommentResponse> commentResponses = articleCommentService.getArticleComments(articleId).stream()
                .map(ArticleCommentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentResponses);
    }

    /**
     * return comment specified
     * @param boardId board id
     * @param articleId article id
     * @param commentId comment id
     * @return comment info
     */
    @GetMapping("{commentId}")
    public ResponseEntity<ArticleCommentResponse> getArticleComment(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @PathVariable("commentId") String commentId
    ) {
        ArticleCommentResponse commentResponse = articleCommentService.getArticleComment(articleId, commentId)
                .map(ArticleCommentResponse::from)
                .orElseThrow(()-> new DataNotFoundException(commentId));
        return ResponseEntity.ok(commentResponse);
    }

    /**
     * save article comment
     * @param boardId board id
     * @param articleId article id
     * @param articleCommentRequest comment request
     * @return void
     */
    @PostMapping
    @Transactional
    public ResponseEntity<ArticleComment> createArticleComment(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @RequestBody ArticleCommentRequest articleCommentRequest
    ) {
        // check anonymous user
        if(!SecurityUtils.isAuthenticated()) {
            if(articleCommentRequest.getUserName() == null) {
                throw new RuntimeException("userName required");
            }
            if(articleCommentRequest.getPassword() == null) {
                throw new RuntimeException("password required");
            }
        }

        // create
        ArticleComment articleComment = ArticleComment.builder()
                .articleId(articleId)
                .commentId(articleCommentRequest.getCommentId())
                .parentCommentId(articleCommentRequest.getParentCommentId())
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

        // save
        articleComment = articleCommentService.saveArticleComment(articleComment);
        return ResponseEntity.ok(articleComment);
    }

    /**
     * edit comment
     * @param boardId board id
     * @param articleId article id
     * @param commentId comment id
     * @return modified comment
     */
    @PutMapping("{commentId}")
    @Transactional
    public ResponseEntity<ArticleCommentResponse> editArticleComment(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @PathVariable("commentId") String commentId,
            @RequestBody ArticleCommentRequest articleCommentRequest
    ) {
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
        articleComment.setContent(articleCommentRequest.getContent());

        // save
        articleComment = articleCommentService.saveArticleComment(articleComment);
        return ResponseEntity.ok(ArticleCommentResponse.from(articleComment));
    }

    /**
     * delete comment
     * @param boardId board id
     * @param articleId article id
     * @param commentId comment id
     * @return void
     */
    @DeleteMapping("{commentId}")
    @Transactional
    public ResponseEntity<Void> deleteArticleComment(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @PathVariable("commentId") String commentId,
            @RequestBody ArticleCommentRequest articleCommentRequest
    ) {
        // get target article comment to delete
        ArticleComment articleComment = articleCommentService.getArticleComment(articleId, commentId).orElseThrow();

        // writer is anonymous user
        if(articleComment.getUserId() == null) {
            if(!passwordEncoder.matches(articleCommentRequest.getPassword(), articleComment.getPassword())) {
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
