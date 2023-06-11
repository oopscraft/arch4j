package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Article;
import org.oopscraft.arch4j.core.board.ArticleComment;
import org.oopscraft.arch4j.core.board.ArticleSearch;
import org.oopscraft.arch4j.core.board.ArticleService;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.oopscraft.arch4j.web.support.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/board/{boardId}")
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleService articleService;

    /**
     * create article
     * @param boardId board id
     * @param articleRequest article info
     */
    @PostMapping("article")
    @Operation(summary = "create new article")
    public ResponseEntity<ArticleResponse> createArticle(@PathVariable("boardId") String boardId, @RequestBody ArticleRequest articleRequest) {
        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .boardId(boardId)
                .userName(articleRequest.getUserName())
                .password(articleRequest.getPassword())
                .build();
        article = articleService.saveArticle(article);
        return ResponseEntity.ok(ArticleResponse.from(article));
    }

    /**
     * modify article
     * @param boardId board id
     * @param articleId article id
     * @param articleRequest article request
     * @return saved article
     */
    @PutMapping("article/{articleId}")
    @Operation(summary = "modify article")
    public ResponseEntity<ArticleResponse> modifyArticle(@PathVariable("boardId")String boardId, @PathVariable("articleId")String articleId, @RequestBody ArticleRequest articleRequest) {
        Article article = articleService.getArticle(articleId).orElseThrow(RuntimeException::new);
        article.setTitle(articleRequest.getTitle());
        article.setContent(articleRequest.getContent());
        article.setPassword(articleRequest.getPassword());
        Article savedArticle = articleService.saveArticle(article);
        return ResponseEntity.ok(ArticleResponse.from(savedArticle));
    }

    /**
     * returns board article list
     * @param boardId board id
     * @param pageable pagination info
     * @return article list
     */
    @GetMapping("article")
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
     * return article
     * @param boardId board id
     * @param articleId article id
     * @return article
     */
    @GetMapping("article/{articleId}")
    @Operation(summary = "get article")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable("boardId")String boardId, @PathVariable("articleId")String articleId) {
        ArticleResponse articleResponse = articleService.getArticle(articleId)
                .map(ArticleResponse::from)
                .orElseThrow(() -> new DataNotFoundException(articleId));
        return ResponseEntity.ok(articleResponse);
    }

    /**
     * save article comment
     * @param boardId board id
     * @param articleId article id
     * @param commentRequest comment request
     * @return void
     */
    @PostMapping("article/{articleId}/comment")
    public ResponseEntity<ArticleComment> createArticleComment(@PathVariable String boardId, @PathVariable String articleId, @RequestBody ArticleCommentRequest commentRequest) {
        ArticleComment articleComment = ArticleComment.builder()
                .articleId(articleId)
                .commentId(commentRequest.getCommentId())
                .parentCommentId(commentRequest.getParentCommentId())
                .content(commentRequest.getContent())
                .userId(SecurityUtils.getCurrentUserId())
                .userName(commentRequest.getUserName())
                .password(commentRequest.getPassword())
                .build();
        articleComment = articleService.saveArticleComment(articleComment);
        return ResponseEntity.ok(articleComment);
    }

    /**
     * return comment list
     * @param boardId board id
     * @param id article id
     * @return comment list
     */
    @GetMapping("article/{id}/comment")
    @Operation(summary = "get article replies")
    public ResponseEntity<List<ArticleCommentResponse>> getArticleComments(@PathVariable("boardId") String boardId, @PathVariable("id") String id) {
        List<ArticleCommentResponse> commentResponses = articleService.getArticleComments(id).stream()
                .map(ArticleCommentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentResponses);
    }

    @PutMapping("article/{articleId}/comment/{commentId}")
    public ResponseEntity<ArticleCommentResponse> modifyArticleComment(@PathVariable String boardId, @PathVariable String articleId, @PathVariable String commentId, @RequestBody ArticleCommentRequest commentRequest) {
        ArticleComment articleComment = articleService.getArticleComment(articleId, commentId).orElseThrow(()->new DataNotFoundException(commentId));
        articleComment.setContent(commentRequest.getContent());
        articleComment = articleService.saveArticleComment(articleComment);
        return ResponseEntity.ok(ArticleCommentResponse.from(articleComment));
    }


    /**
     * return comment specified
     * @param boardId board id
     * @param articleId article id
     * @param commentId comment id
     * @return comment info
     */
    @GetMapping("article/{articleId}/comment/{commentId}")
    public ResponseEntity<ArticleCommentResponse> getArticleComment(@PathVariable("boardId") String boardId, @PathVariable("articleId")String articleId, @PathVariable("commentId")String commentId) {
        ArticleCommentResponse commentResponse = articleService.getArticleComment(articleId, commentId)
                .map(ArticleCommentResponse::from)
                .orElseThrow(()-> new DataNotFoundException(commentId));
        return ResponseEntity.ok(commentResponse);
    }

}
