package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.board.repository.ArticleEntity;
import org.oopscraft.arch4j.core.board.repository.ArticleFileEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class ArticleServiceTest extends CoreTestSupport {

    final ArticleService articleService;

    Article testArticle = Article.builder()
            .articleId(null)
            .title("test title")
            .content("test content")
            .boardId("test")
            .userId("test")
            .userName("test")
            .password("password")
            .build();

    ArticleFile testArticleFile = ArticleFile.builder()
            .articleId("article id")
            .fileId(null)
            .build();

    @Test
    @Order(1)
    void saveArticle() {
        Article savedArticle = articleService.saveArticle(testArticle);
        assertNotNull(entityManager.find(ArticleEntity.class, savedArticle.getArticleId()));
    }

    @Test
    @Order(2)
    void getArticle() {
        Article savedArticle = articleService.saveArticle(testArticle);
        Article article = articleService.getArticle(savedArticle.getArticleId()).orElse(null);
        assertNotNull(article);
    }

    @Test
    @Order(3)
    void deleteArticle() {
        Article savedArticle = articleService.saveArticle(testArticle);saveArticle();
        articleService.deleteArticle(savedArticle.getArticleId());
        assertNull(entityManager.find(ArticleEntity.class, savedArticle.getArticleId()));
    }

    @Test
    @Order(4)
    void getArticles() {
        Article savedArticle = articleService.saveArticle(testArticle);
        ArticleSearch articleSearch = ArticleSearch.builder()
                .boardId(savedArticle.getBoardId())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Article> articlePage = articleService.getArticles(articleSearch, pageable);
        assertTrue(articlePage.getContent().stream().anyMatch(e -> e.getBoardId().equals(articleSearch.getBoardId())));
    }

    @Test
    @Order(5)
    void saveArticleFile() {

        // save
        ArticleFile savedArticleFile = articleService.saveArticleFile(testArticleFile);

        // check
        ArticleFileEntity.Pk pk = ArticleFileEntity.Pk.builder()
                .articleId(savedArticleFile.getArticleId())
                .fileId(savedArticleFile.getFileId())
                .build();
        assertNotNull(entityManager.find(ArticleFileEntity.class, pk));
    }

    @Test
    @Order(6)
    void getArticleFiles() {
        ArticleFile savedArticleFile = articleService.saveArticleFile(testArticleFile);
        List<ArticleFile> articleFiles = articleService.getArticleFiles(savedArticleFile.getArticleId());
        assertFalse(articleFiles.isEmpty());
    }

}