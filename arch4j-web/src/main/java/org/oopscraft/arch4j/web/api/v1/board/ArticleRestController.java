package org.oopscraft.arch4j.web.api.v1.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.*;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.web.api.v1.board.dto.ArticleDeleteRequest;
import org.oopscraft.arch4j.web.api.v1.board.dto.ArticleRequest;
import org.oopscraft.arch4j.web.api.v1.board.dto.ArticleResponse;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/boards/{boardId}/articles")
@RequiredArgsConstructor
@Tag(name = "board")
public class ArticleRestController {

    private final BoardService boardService;

    private final ArticleService articleService;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;

    @GetMapping
    @Operation(summary = "get list of articles")
    @PreAuthorize("@boardPermissionEvaluator.canReadArticle(#boardId)")
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
        // get board info
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check access permission
        SecurityUtils.checkPermission(board.getAccessPolicy(), board.getAccessRoles());

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@boardPermissionEvaluator.canWriteArticle(#boardId)")
    @Transactional
    @Operation(summary = "create article")
    public ResponseEntity<ArticleResponse> createArticle(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "article", schema = @Schema(type="object", implementation = ArticleRequest.class))
            @RequestPart("article") String articleRequestString,
            @Parameter(description = "attachment files")
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) {
        // multipart article string to object (converter is not work)
        ArticleRequest articleRequest;
        try {
            articleRequest = this.objectMapper.readValue(articleRequestString, ArticleRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // get board info
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

    @GetMapping("{articleId}")
    @PreAuthorize("@boardPermissionEvaluator.canReadArticle(#boardId)")
    @Operation(summary = "get article")
    public ResponseEntity<ArticleResponse> getArticle(
            @Parameter(description = "board ID")
            @PathVariable("boardId")String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId")String articleId
    ) {
        // get board info
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check permission
        SecurityUtils.checkPermission(board.getAccessPolicy(), board.getAccessRoles());
        SecurityUtils.checkPermission(board.getReadPolicy(), board.getReadRoles());

        Article article = articleService.getArticle(articleId).orElseThrow();
        ArticleResponse articleResponse = ArticleResponse.from(article);

        // set permission
        if(article.getUserId() != null) {   // authenticated user
            if(article.getUserId().equals(SecurityUtils.getCurrentUserId())) {
                articleResponse.setCanModify(true);
                articleResponse.setCanDelete(true);
            }
        }else{                              // anonymous user
            articleResponse.setCanModify(true);
            articleResponse.setCanDelete(true);
        }

        return ResponseEntity.ok(articleResponse);
    }

    @PutMapping("{articleId}")
    @Transactional
    @PreAuthorize("@boardPermissionEvaluator.canWriteArticle(#boardId)")
    @Operation(summary = "modify article")
    public ResponseEntity<ArticleResponse> modifyArticle(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId") String articleId,
            @Parameter(description = "article", schema = @Schema(type="object", implementation = ArticleRequest.class))
            @RequestPart("article") String articleRequestString,
            @Parameter(description = "attachment files")
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) {
        // multipart article string to object (converter is not work)
        ArticleRequest articleRequest;
        try {
            articleRequest = this.objectMapper.readValue(articleRequestString, ArticleRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // get board info
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check permission
        SecurityUtils.checkPermission(board.getAccessPolicy(), board.getAccessRoles());
        SecurityUtils.checkPermission(board.getReadPolicy(), board.getReadRoles());
        SecurityUtils.checkPermission(board.getWritePolicy(), board.getWriteRoles());

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

    @DeleteMapping("{articleId}")
    @Transactional
    @PreAuthorize("@boardPermissionEvaluator.canWriteArticle(#boardId)")
    @Operation(description = "delete article")
    public ResponseEntity<Void> deleteArticle(
            @Parameter(description = "board ID")
            @PathVariable("boardId") String boardId,
            @Parameter(description = "article ID")
            @PathVariable("articleId") String articleId,
            @Parameter(description = "article delete request", schema = @Schema(type = "object", implementation = ArticleDeleteRequest.class))
            @RequestBody ArticleDeleteRequest articleDeleteRequest
    ) {
        // get board info
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check permission
        SecurityUtils.checkPermission(board.getAccessPolicy(), board.getAccessRoles());
        SecurityUtils.checkPermission(board.getReadPolicy(), board.getReadRoles());
        SecurityUtils.checkPermission(board.getWritePolicy(), board.getWriteRoles());

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
        // get board info
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check permission
        SecurityUtils.checkPermission(board.getAccessPolicy(), board.getAccessRoles());
        SecurityUtils.checkPermission(board.getReadPolicy(), board.getReadRoles());

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
