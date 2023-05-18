package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.board.repository.ArticleEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class ArticleServiceTest extends CoreTestSupport {

    final ArticleService articleService;

    Article testArticle = Article.builder()
            .id(UUID.randomUUID().toString())
            .title("test title")
            .content("test content")
            .boardId("test")
            .build();

    @Test
    @Order(1)
    void saveArticle() {
        articleService.saveArticle(testArticle);
        assertNotNull(entityManager.find(ArticleEntity.class, testArticle.getId()));
    }

    @Test
    @Order(2)
    void getArticle() {

        // save test
        saveArticle();

        // get article
        Article article = articleService.getArticle(testArticle.getId()).orElse(null);

        // check
        assertNotNull(article);
    }

    @Test
    @Order(3)
    void deleteArticle() {

        // save test user
        saveArticle();

        // delete
        articleService.deleteArticle(testArticle.getId());

        // check
        assertNull(entityManager.find(ArticleEntity.class, testArticle.getId()));
    }

    @Test
    @Order(4)
    void getArticles() {

        // save test user
        saveArticle();

        // get users by condition
        ArticleSearch articleSearch = ArticleSearch.builder()
                .boardId(testArticle.getBoardId())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Article> articlePage = articleService.getArticles(articleSearch, pageable);

        // check result
        assertTrue(articlePage.getContent().stream().anyMatch(e -> e.getBoardId().equals(articleSearch.getBoardId())));
    }

}