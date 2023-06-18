package org.oopscraft.arch4j.web.api.v1.board;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.board.TextFormat;
import org.oopscraft.arch4j.web.test.WebTestSupport;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ArticleRestControllerTest extends WebTestSupport {

    ArticleRequest testArticleRequest = ArticleRequest.builder()
            .articleId(null)
            .title("test title")
            .contentFormat(TextFormat.PLAIN)
            .content("test content")
            .build();

    @Test
    @Order(1)
    void saveArticle() throws Exception {

        MockMultipartFile article = new MockMultipartFile(
                "article",
                "",
                "application/json",
                objectMapper.writeValueAsString(testArticleRequest).getBytes(StandardCharsets.UTF_8)
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/board/anonymous/article")
                        .file(article)
                        .param("some-random", "4"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void getArticles() throws Exception {
        saveArticle();
        this.mockMvc.perform(get("/api/v1/board/anonymous/article"))
                .andDo(print()).andExpect(status().isOk());
    }

}