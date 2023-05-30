package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Article;
import org.oopscraft.arch4j.core.board.ArticleSearch;
import org.oopscraft.arch4j.core.board.ArticleService;
import org.oopscraft.arch4j.core.comment.Comment;
import org.oopscraft.arch4j.core.comment.CommentService;
import org.oopscraft.arch4j.web.api.v1.comment.CommentRequest;
import org.oopscraft.arch4j.web.api.v1.comment.CommentResponse;
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
     * save article
     * @param boardId board id
     * @param articleRequest article info
     */
    @PostMapping("article")
    @Operation(summary = "save article info")
    public ResponseEntity<Void> saveArticle(@PathVariable("boardId") String boardId, @RequestBody ArticleRequest articleRequest) {
        Article article = Article.builder()
                .id(articleRequest.getId())
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .boardId(boardId)
                .build();
        articleService.saveArticle(article);
        return ResponseEntity.ok().build();
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
     * @param id article id
     * @return article
     */
    @GetMapping("article/{id}")
    @Operation(summary = "get article")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable("boardId")String boardId, @PathVariable("id")String id) {
        ArticleResponse articleResponse = articleService.getArticle(id)
                .map(ArticleResponse::from)
                .orElseThrow(() -> new DataNotFoundException(id));
        return ResponseEntity.ok(articleResponse);
    }

    /**
     * save article comment
     * @param boardId board id
     * @param id article id
     * @param commentRequest comment request
     * @return void
     */
    @PostMapping("article/{id}/comment")
    public ResponseEntity<Void> saveArticleComment(@PathVariable String boardId, @PathVariable String id, @RequestBody CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .parentId(commentRequest.getParentId())
                .content(commentRequest.getContent())
                .build();
        articleService.saveArticleComment(id, comment);
        return ResponseEntity.ok().build();
    }

    /**
     * return comment list
     * @param boardId board id
     * @param id article id
     * @return comment list
     */
    @GetMapping("article/{id}/comment")
    @Operation(summary = "get article replies")
    public ResponseEntity<List<CommentResponse>> getArticleComments(@PathVariable("boardId") String boardId, @PathVariable("id") String id) {
        List<CommentResponse> commentResponses = articleService.getArticleComments(id).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentResponses);
    }


}
