package org.oopscraft.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Article;
import org.oopscraft.arch4j.core.board.ArticleFile;
import org.oopscraft.arch4j.core.board.ArticleSearch;
import org.oopscraft.arch4j.core.board.ArticleService;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.oopscraft.arch4j.web.support.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/board/{boardId}/article")
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleService articleService;

    private final PasswordEncoder passwordEncoder;

    /**
     * returns board article list
     * @param boardId board id
     * @param pageable pagination info
     * @return article list
     */
    @GetMapping
    @Operation(summary = "get list of articles")
    public ResponseEntity<List<ArticleResponse>> getArticles(
            @PathVariable("boardId") String boardId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            Pageable pageable
    ) {
        ArticleSearch articleSearch = ArticleSearch.builder()
                .boardId(boardId)
                .title(title)
                .content(content)
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
    @GetMapping("{articleId}")
    @Operation(summary = "get article")
    public ResponseEntity<ArticleResponse> getArticle(
            @PathVariable("boardId")String boardId,
            @PathVariable("articleId")String articleId
    ) {
        ArticleResponse articleResponse = articleService.getArticle(articleId)
                .map(ArticleResponse::from)
                .orElseThrow(() -> new DataNotFoundException(articleId));
        return ResponseEntity.ok(articleResponse);
    }

    /**
     * create article
     * @param boardId board id
     * @param articleRequest article info
     */
    @PostMapping
    @Operation(summary = "create new article")
    @Transactional
    public ResponseEntity<ArticleResponse> createArticle(
            @PathVariable("boardId") String boardId,
            @RequestPart("article") ArticleRequest articleRequest,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) {
        // check anonymous user
        if(!SecurityUtils.isAuthenticated()) {
            if(articleRequest.getUserName() == null) {
                throw new RuntimeException("userName required");
            }
            if(articleRequest.getPassword() == null) {
                throw new RuntimeException("password required");
            }
        }

        // create
        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .boardId(boardId)
                .files(articleRequest.getFiles().stream()
                        .map(articleFileRequest ->
                            ArticleFile.builder()
                                    .articleId(articleFileRequest.getArticleId())
                                    .fileId(articleFileRequest.getFileId())
                                    .filename(articleFileRequest.getFilename())
                                    .contentType(articleFileRequest.getContentType())
                                    .length(articleFileRequest.getLength())
                                    .build()
                        ).collect(Collectors.toList()))
                .build();

        // authenticated user
        if(SecurityUtils.isAuthenticated()) {
            article.setUserId(SecurityUtils.getCurrentUserId());
        }
        // anonymous user
        else{
            article.setUserId(null);
            article.setUserName(articleRequest.getUserName());
            article.setPassword(articleRequest.getPassword());
        }

        // save
        article = articleService.saveArticle(article, files);
        return ResponseEntity.ok(ArticleResponse.from(article));
    }

    /**
     * edit article
     * @param boardId board id
     * @param articleId article id
     * @param articleRequest article request
     * @return saved article
     */
    @PutMapping("{articleId}")
    @Operation(summary = "edit article")
    @Transactional
    public ResponseEntity<ArticleResponse> editArticle(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @RequestPart("article") ArticleRequest articleRequest,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) {
        // get article
        Article article = articleService.getArticle(articleId).orElseThrow();

        // writer is anonymous user
        if(article.getUserId() == null) {
            if(!passwordEncoder.matches(articleRequest.getPassword(), article.getPassword())){
                throw new RuntimeException("password not matches");
            }
        }
        // writer is authenticated user
        else {
            if(!Objects.equals(SecurityUtils.getCurrentUserId(), article.getUserId())) {
                throw new RuntimeException("user is not writer");
            }
        }

        // change articles
        article.setTitle(articleRequest.getTitle());
        article.setContent(articleRequest.getContent());
        article.setUserName(articleRequest.getUserName());
        article.setPassword(articleRequest.getPassword());
        article.setFiles(articleRequest.getFiles().stream()
                .map(articleFileRequest ->
                    ArticleFile.builder()
                        .articleId(articleFileRequest.getArticleId())
                        .fileId(articleFileRequest.getFileId())
                        .filename(articleFileRequest.getFilename())
                        .contentType(articleFileRequest.getContentType())
                        .length(articleFileRequest.getLength())
                        .build()
                )
                .collect(Collectors.toList()));

        // save article
        article = articleService.saveArticle(article, files);
        return ResponseEntity.ok(ArticleResponse.from(article));
    }

    /**
     * get article file
     * @param articleId article id
     * @param fileId file id
     * @param response http servlet response
     */
    @GetMapping("{articleId}/file/{fileId}")
    public ResponseEntity<Void> getArticleFile(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @PathVariable("fileId") String fileId,
            HttpServletResponse response
    ) {
        ArticleFile articleFile = articleService.getArticleFile(articleId, fileId).orElseThrow();
        response.setHeader("Content-Disposition",String.format("attachment; filename=\"%s\";", articleFile.getFilename()));
        try (InputStream inputStream = articleService.getArticleFileInputStream(articleFile)) {
            StreamUtils.copy(inputStream, response.getOutputStream());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * delete article
     * @param boardId board id
     * @param articleId article id
     * @param articleRequest article request
     * @return void
     */
    @DeleteMapping("{articleId}")
    @Transactional
    public ResponseEntity<Void> deleteArticle(
        @PathVariable("boardId") String boardId,
        @PathVariable("articleId") String articleId,
        @RequestBody ArticleRequest articleRequest
    ) {
        // get target article
        Article article = articleService.getArticle(articleId).orElseThrow();

        // writer is anonymous user
        if(article.getUserId() == null) {
            if(!passwordEncoder.matches(articleRequest.getPassword(), article.getPassword())) {
                throw new RuntimeException("password not match");
            }
        }
        // writer is authenticated user
        else {
            if (!Objects.equals(article.getUserId(), SecurityUtils.getCurrentUserId())) {
                throw new RuntimeException("not writer");
            }
        }

        // delete article
        articleService.deleteArticle(articleId);
        return ResponseEntity.ok().build();
    }

}
