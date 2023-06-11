package org.oopscraft.arch4j.core.board;

import antlr.debug.ParserTokenListener;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.repository.*;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.data.ValidationUtils;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final ArticleFileRepository articleFileRepository;

    private final ArticleCommentRepository articleCommentRepository;

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
        if(article.getArticleId() == null) {
            articleEntity = ArticleEntity.builder()
                    .articleId(IdGenerator.uuid())
                    .createdAt(LocalDateTime.now())
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
            articleEntity = articleRepository.findById(article.getArticleId()).orElseThrow(RuntimeException::new);

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

        // save
        articleEntity = articleRepository.saveAndFlush(articleEntity);
        return Article.from(articleEntity);
    }

    /**
     * returns article info
     * @param articleId article id
     * @return article info
     */
    public Optional<Article> getArticle(String articleId) {
        return articleRepository.findById(articleId).map(Article::from);
    }

    /**
     * deletes article
     * @param articleId article id
     */
    public void deleteArticle(String articleId) {
        articleRepository.deleteById(articleId);
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
     * save article file
     * @param articleFile article file
     * @return saved article file
     */
    public ArticleFile saveArticleFile(ArticleFile articleFile) {
        ArticleFileEntity.Pk pk = ArticleFileEntity.Pk.builder()
                .articleId(articleFile.getArticleId())
                .fileId(articleFile.getFileId())
                .build();
        ArticleFileEntity articleFileEntity = articleFileRepository.findById(pk).orElse(null);
        if(articleFileEntity == null) {
            articleFileEntity = ArticleFileEntity.builder()
                    .articleId(articleFile.getArticleId())
                    .fileId(IdGenerator.uuid())
                    .build();
        }
        articleFileEntity.setFilename(articleFile.getFilename());
        articleFileEntity.setContentType(articleFile.getContentType());
        articleFileEntity.setLength(articleFile.getLength());
        articleFileEntity = articleFileRepository.saveAndFlush(articleFileEntity);

        // TODO file upload

        return ArticleFile.from(articleFileEntity);
    }

    /**
     * get article files
     * @param articleId article id
     * @return article files
     */
    public List<ArticleFile> getArticleFiles(String articleId) {
        return articleFileRepository.findAllByArticleIdOrderByCreatedAtAsc(articleId).stream()
                .map(ArticleFile::from)
                .collect(Collectors.toList());
    }

    public Optional<ArticleFile> getArticleFile(String articleId, String fileId) {
        ArticleFileEntity.Pk pk = ArticleFileEntity.Pk.builder()
                .articleId(articleId)
                .fileId(fileId)
                .build();
        ArticleFile articleFile = articleFileRepository.findById(pk)
                .map(ArticleFile::from)
                .orElse(null);
        return Optional.ofNullable(articleFile);
    }

    public void deleteArticleFile(String articleId, String id) {
        ArticleFileEntity.Pk pk = ArticleFileEntity.Pk.builder()
                .articleId(articleId)
                .fileId(id)
                .build();
        articleFileRepository.deleteById(pk);
        articleFileRepository.flush();

        // TODO delete file
    }

    /**
     * save article comment
     * @param articleComment article comment
     * @return article comment
     */
    public ArticleComment saveArticleComment(ArticleComment articleComment) {
        ArticleCommentEntity.Pk pk = ArticleCommentEntity.Pk.builder()
                .articleId(articleComment.getArticleId())
                .commentId(articleComment.getCommentId())
                .build();
        ArticleCommentEntity articleCommentEntity = articleCommentRepository.findById(pk).orElse(null);

        // create new article comment
        if(articleCommentEntity == null) {
            articleCommentEntity = ArticleCommentEntity.builder()
                    .articleId(articleComment.getArticleId())
                    .commentId(IdGenerator.uuid())
                    .parentCommentId(articleComment.getParentCommentId())
                    .createdAt(LocalDateTime.now())
                    .build();
            // authenticated user
            if(SecurityUtils.isAuthenticated()) {
                articleCommentEntity.setUserId(SecurityUtils.getCurrentUserId());
            }
            // anonymous user
            else {
                // userName required
                if(articleComment.getUserName() == null) {
                    throw new RuntimeException("userName is required");
                }
                articleCommentEntity.setUserName(articleComment.getUserName());
                // password required
                if(articleComment.getPassword() == null) {
                    throw new RuntimeException("password is required");
                }
                articleCommentEntity.setPassword(articleComment.getPassword());
            }
        }
        // modify previous article comment
        else {
            // check current user is match to writer
            if(articleCommentEntity.getUserId() != null) {
                if(!articleCommentEntity.getUserId().equals(SecurityUtils.getCurrentUserId())){
                    throw new RuntimeException("not matches user id");
                }
            }
            // if anonymous user's article, check password
            else{
                if(!passwordEncoder.matches(articleComment.getPassword(), articleCommentEntity.getPassword())) {
                    throw new RuntimeException("password not matches");
                }
            }
        }

        articleCommentEntity.setContent(articleComment.getContent());
        articleCommentEntity = articleCommentRepository.saveAndFlush(articleCommentEntity);
        return ArticleComment.from(articleCommentEntity);
    }

    /**
     * return article comments by article id
     * @param articleId article id
     * @return article comment list
     */
    public List<ArticleComment> getArticleComments(String articleId) {
        return articleCommentRepository.findAllByArticleIdOrderByCreatedAtAsc(articleId).stream()
                .map(ArticleComment::from)
                .collect(Collectors.toList());
    }

    /**
     * return article comment
     * @param articleId article id
     * @param commentId comment id
     * @return article comment
     */
    public Optional<ArticleComment> getArticleComment(String articleId, String commentId) {
        ArticleCommentEntity.Pk pk = ArticleCommentEntity.Pk.builder()
                .articleId(articleId)
                .commentId(commentId)
                .build();
        return articleCommentRepository.findById(pk)
                .map(ArticleComment::from);
    }

    /**
     * delete article comment
     * @param articleId article id
     * @param commentId comment id
     */
    public void deleteArticleComment(String articleId, String commentId) {
        ArticleCommentEntity.Pk pk = ArticleCommentEntity.Pk.builder()
                .articleId(articleId)
                .commentId(commentId)
                .build();
        articleCommentRepository.deleteById(pk);
        articleCommentRepository.flush();
    }

}
