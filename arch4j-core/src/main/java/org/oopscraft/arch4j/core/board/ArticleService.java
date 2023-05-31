package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.repository.ArticleEntity;
import org.oopscraft.arch4j.core.board.repository.ArticleRepository;
import org.oopscraft.arch4j.core.comment.Comment;
import org.oopscraft.arch4j.core.comment.CommentService;
import org.oopscraft.arch4j.core.file.FileInfo;
import org.oopscraft.arch4j.core.file.FileService;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.file.repository.FileInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final FileService fileService;

    private final CommentService commentService;

    /**
     * saves article
     * @param article article info
     */
    public void saveArticle(Article article) {
        ArticleEntity articleEntity = null;
        if(article.getId() != null) {
            articleEntity = articleRepository.findById(article.getId()).orElse(null);
        }
        if(articleEntity == null) {
            articleEntity = ArticleEntity.builder()
                    .id(IdGenerator.uuid())
                    .dateTime(LocalDateTime.now())
                    .build();
        }
        articleEntity.setTitle(article.getTitle());
        articleEntity.setContent(article.getContent());
        articleEntity.setBoardId(article.getBoardId());

        // files
        articleEntity.setFiles(article.getFiles().stream()
                .map(fileInfo -> {
                    return FileInfoEntity.builder()
                            .id(fileInfo.getId())
                            .filename(fileInfo.getFilename())
                            .contentType(fileInfo.getContentType())
                            .length(fileInfo.getLength())
                            .build();
                }).collect(Collectors.toList()));

        // save
        articleRepository.saveAndFlush(articleEntity);
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
     */
    public void saveArticleComment(String articleId, Comment comment) {
        commentService.saveComment("ARTICLE", articleId, comment);
    }

    /**
     * returns article comments
     * @param articleId articleId
     * @return return comments
     */
    public List<Comment> getArticleComments(String articleId) {
        return commentService.getComments("ARTICLE", articleId);
    }

}
