package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.repository.ArticleEntity;
import org.oopscraft.arch4j.core.board.repository.ArticleRepository;
import org.oopscraft.arch4j.core.comment.Comment;
import org.oopscraft.arch4j.core.comment.CommentService;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.data.ValidationUtils;
import org.oopscraft.arch4j.core.file.repository.FileInfoEntity;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final CommentService commentService;

    private final PasswordEncoder passwordEncoder;

    /**
     * saves article
     * @param article article info
     * @return article
     */
    public Article saveArticle(Article article) {
        ValidationUtils.validate(article);  // validate
        ArticleEntity articleEntity;

        // create new article
        if(article.getId() == null) {
            articleEntity = ArticleEntity.builder()
                    .id(IdGenerator.uuid())
                    .dateTime(LocalDateTime.now())
                    .build();

            // authenticated user
            if(SecurityUtils.isAuthenticated()) {
                articleEntity.setUserId(SecurityUtils.getCurrentUserId());
            }
            // anonymous user
            else{
                // userName required
                if(article.getUserName() == null) {
                    throw new RuntimeException("userName is required");
                }
                articleEntity.setUserName(article.getUserName());

                // password required
                if(article.getPassword() == null) {
                    throw new RuntimeException("password is required.");
                }
                articleEntity.setPassword(passwordEncoder.encode(article.getPassword()));
            }
        }
        // modify previous article
        else {
            articleEntity = articleRepository.findById(article.getId()).orElseThrow(RuntimeException::new);

            // check current user is match to writer
            if(articleEntity.getUserId() != null) {
                if(!articleEntity.getUserId().equals(SecurityUtils.getCurrentUserId())){
                    throw new RuntimeException("not matches user id");
                }
            }
            // if anonymous user's article, check password
            else{
                if(!passwordEncoder.matches(article.getPassword(), articleEntity.getPassword())) {
                    throw new RuntimeException("password not matches");
                }
            }
        }

        // set property
        articleEntity.setTitle(article.getTitle());
        articleEntity.setContent(article.getContent());
        articleEntity.setBoardId(article.getBoardId());

        // files
        articleEntity.setFiles(article.getFiles().stream()
                .map(fileInfo -> FileInfoEntity.builder()
                        .id(fileInfo.getId())
                        .filename(fileInfo.getFilename())
                        .contentType(fileInfo.getContentType())
                        .length(fileInfo.getLength())
                        .build())
                .collect(Collectors.toList()));

        // save
        articleEntity = articleRepository.saveAndFlush(articleEntity);
        return Article.from(articleEntity);
    }

    /**
     * returns article info
     * @param id article id
     * @return article info
     */
    public Optional<Article> getArticle(String id) {
        return articleRepository.findById(id).map(Article::from);
    }

    /**
     * deletes article
     * @param id article id
     */
    public void deleteArticle(String id) {
        articleRepository.deleteById(id);
        articleRepository.flush();
    }

    /**
     * returns articles
     * @param articleSearch article search condition
     * @param pageable pagination info
     * @return list of article
     */
    public Page<Article> getArticles(ArticleSearch articleSearch, Pageable pageable) {
        Page<ArticleEntity> articleEntityPage = articleRepository.findArticles(articleSearch, pageable);
        List<Article> articles = articleEntityPage.getContent().stream()
                .map(Article::from)
                .collect(Collectors.toList());
        long total = articleEntityPage.getTotalElements();
        return new PageImpl<>(articles, pageable, total);
    }

    /**
     * save article comment
     * @param articleId article id
     * @param comment comment info
     * @return comment
     */
    public Comment saveArticleComment(String articleId, Comment comment) {
        return commentService.saveComment("ARTICLE", articleId, comment);
    }

    /**
     * returns article comments
     * @param articleId articleId
     * @return return comments
     */
    public List<Comment> getArticleComments(String articleId) {
        return commentService.getComments("ARTICLE", articleId);
    }

    /**
     * return article comment
     * @param articleId article id
     * @param commentId comment id
     * @return comment info
     */
    public Optional<Comment> getArticleComment(String articleId, String commentId) {
        return commentService.getComment(commentId);
    }

    /**
     * delete comment
     * @param articleId article id
     * @param commentId comment id
     */
    public void deleteArticleComment(String articleId, String commentId) {
        commentService.deleteComment(commentId);
    }
}
