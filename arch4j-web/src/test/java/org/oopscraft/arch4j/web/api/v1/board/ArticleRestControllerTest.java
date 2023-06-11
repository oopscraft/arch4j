package org.oopscraft.arch4j.web.api.v1.board;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.oopscraft.arch4j.core.sample.Sample;
import org.oopscraft.arch4j.core.sample.SampleType;
import org.oopscraft.arch4j.web.api.v1.board.ArticleRequest;
import org.oopscraft.arch4j.web.test.WebTestSupport;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ArticleRestControllerTest extends WebTestSupport {

    ArticleRequest testArticleRequest = ArticleRequest.builder()
            .articleId(null)
            .title("test title")
            .content("test content")
            .build();

    @Test
    @Order(1)
    void saveArticle() throws Exception {
        this.mockMvc.perform(post("/api/v1/board/test/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testArticleRequest))
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void getArticles() throws Exception {
        saveArticle();
        this.mockMvc.perform(get("/api/v1/board/test/article"))
                .andDo(print()).andExpect(status().isOk());
    }

}