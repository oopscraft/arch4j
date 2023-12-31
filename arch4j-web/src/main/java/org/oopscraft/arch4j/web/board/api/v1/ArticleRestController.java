package org.oopscraft.arch4j.web.board.api.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.*;
import org.oopscraft.arch4j.web.board.BoardPermissionEvaluator;
import org.oopscraft.arch4j.web.board.api.v1.dto.ArticleDeleteRequest;
import org.oopscraft.arch4j.web.board.api.v1.dto.ArticleFileRequest;
import org.oopscraft.arch4j.web.board.api.v1.dto.ArticleRequest;
import org.oopscraft.arch4j.web.board.api.v1.dto.ArticleResponse;
import org.oopscraft.arch4j.web.security.SecurityUtils;
import org.oopscraft.arch4j.web.support.PageableAsQueryParam;
import org.oopscraft.arch4j.web.support.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/board/{boardId}/article")
@RequiredArgsConstructor
@Tag(name = "board")
public class ArticleRestController {

    private final BoardService boardService;

    private final BoardPermissionEvaluator boardPermissionEvaluator;

    private final ArticleService articleService;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@boardPermissionEvaluator.hasWritePermission(#boardId)")
    @Transactional
    @Operation(summary = "create article")
    public ResponseEntity<ArticleResponse> createArticle(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "article", schema = @Schema(type="object", implementation = ArticleRequest.class))
            @RequestPart("article") String articleRequestString,
            @Parameter(description = "attachment files")
            @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles
    ) {
        // multipart article string to object (converter is not work)
        ArticleRequest articleRequest;
        try {
            articleRequest = this.objectMapper.readValue(articleRequestString, ArticleRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // board
        Board board = boardService.getBoard(boardId).orElseThrow();

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
                .contentFormat(articleRequest.getContentFormat())
                .content(articleRequest.getContent())
                .boardId(boardId)
                .files(mapToArticleFiles(articleRequest.getFiles()))
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

        // file
        List<MultipartFile> files = new ArrayList<>();
        if(board.isFileEnabled()) {
            if(multipartFiles != null) {
                if(!boardPermissionEvaluator.hasFilePermission(board)) {
                    throw new RuntimeException("has no file permission");
                }
                this.checkFileSizeLimit(board, multipartFiles);
                files.addAll(Arrays.asList(multipartFiles));
            }
        }

        // save
        article = articleService.saveArticle(article, files);
        return ResponseEntity.ok(ArticleResponse.from(article));
    }


    @PutMapping("{articleId}")
    @Transactional
    @PreAuthorize("@boardPermissionEvaluator.hasWritePermission(#boardId)")
    @Operation(summary = "modify article")
    public ResponseEntity<ArticleResponse> modifyArticle(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId") String articleId,
            @Parameter(description = "article", schema = @Schema(type="object", implementation = ArticleRequest.class))
            @RequestPart("article") String articleRequestString,
            @Parameter(description = "attachment files")
            @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles
    ) {
        // multipart article string to object (converter is not work)
        ArticleRequest articleRequest;
        try {
            articleRequest = this.objectMapper.readValue(articleRequestString, ArticleRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // get board
        Board board = boardService.getBoard(boardId).orElseThrow();

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
        article.setContentFormat(articleRequest.getContentFormat());
        article.setContent(articleRequest.getContent());
        article.setUserName(articleRequest.getUserName());
        article.setPassword(articleRequest.getPassword());
        article.setFiles(mapToArticleFiles(articleRequest.getFiles()));

        // file
        List<MultipartFile> files = new ArrayList<>();
        if(board.isFileEnabled()) {
            if(multipartFiles != null) {
                if(!boardPermissionEvaluator.hasFilePermission(board)) {
                    throw new RuntimeException("has no file permission");
                }
                this.checkFileSizeLimit(board, multipartFiles);
                files.addAll(Arrays.asList(multipartFiles));
            }
        }

        // save article
        article = articleService.saveArticle(article, files);
        return ResponseEntity.ok(ArticleResponse.from(article));
    }

    private void checkFileSizeLimit(Board board, MultipartFile[] multipartFiles) {
        if(board.getFileSizeLimit() != null) {
            for(MultipartFile multipartFile : multipartFiles) {
                long fileSize = multipartFile.getSize();
                if(fileSize/1024/1024 > board.getFileSizeLimit()) {
                    throw new RuntimeException("Exceed file size limit");
                }
            }
        }
    }

    private List<ArticleFile> mapToArticleFiles(List<ArticleFileRequest> articleFileRequests) {
        return articleFileRequests.stream()
                .map(articleFileRequest ->
                        ArticleFile.builder()
                                .articleId(articleFileRequest.getArticleId())
                                .fileId(articleFileRequest.getFileId())
                                .filename(articleFileRequest.getFilename())
                                .contentType(articleFileRequest.getContentType())
                                .length(articleFileRequest.getLength())
                                .build()
                ).collect(Collectors.toList());
    }

    @GetMapping("{articleId}")
    @PreAuthorize("@boardPermissionEvaluator.hasReadPermission(#boardId)")
    @Operation(summary = "get article")
    public ResponseEntity<ArticleResponse> getArticle(
            @Parameter(description = "board ID")
            @PathVariable("boardId")String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId")String articleId
    ) {
        Article article = articleService.getArticle(articleId).orElseThrow();
        ArticleResponse articleResponse = ArticleResponse.from(article);
        return ResponseEntity.ok(articleResponse);
    }

    @DeleteMapping("{articleId}")
    @Transactional
    @PreAuthorize("@boardPermissionEvaluator.hasWritePermission(#boardId)")
    @Operation(description = "delete article")
    public ResponseEntity<Void> deleteArticle(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId") String articleId,
            @Parameter(description = "article delete request", schema = @Schema(type = "object", implementation = ArticleDeleteRequest.class))
            @RequestBody ArticleDeleteRequest articleDeleteRequest
    ) {
        // get target article
        Article article = articleService.getArticle(articleId).orElseThrow();

        // writer is anonymous user
        if(article.getUserId() == null) {
            if(!passwordEncoder.matches(articleDeleteRequest.getPassword(), article.getPassword())) {
                throw new AccessDeniedException("password not match");
            }
        }
        // writer is authenticated user
        else {
            if (!Objects.equals(article.getUserId(), SecurityUtils.getCurrentUserId())) {
                throw new AccessDeniedException("not writer");
            }
        }

        // delete article
        articleService.deleteArticle(articleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "get list of articles")
    @PreAuthorize("@boardPermissionEvaluator.hasAccessPermission(#boardId)")
    @PageableAsQueryParam
    public ResponseEntity<List<ArticleResponse>> getArticles(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "title search keyword")
            @RequestParam(value = "title", required = false) String title,
            @Parameter(description = "content search keyword")
            @RequestParam(value = "content", required = false) String content,
            @Parameter(hidden = true)
                    Pageable pageable
    ) {
        // search articles
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


    @GetMapping("{articleId}/file/{fileId}")
    @Operation(description = "get article file")
    public ResponseEntity<Void> getArticleFile(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId") String articleId,
            @Parameter(description = "file ID")
            @PathVariable("fileId") String fileId,
            HttpServletResponse response
    ) {
        // response
        ArticleFile articleFile = articleService.getArticleFile(articleId, fileId).orElseThrow();
        response.setHeader("Content-Disposition",String.format("attachment; filename=\"%s\";", articleFile.getFilename()));
        try (InputStream inputStream = articleService.getArticleFileInputStream(articleFile)) {
            StreamUtils.copy(inputStream, response.getOutputStream());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

}
