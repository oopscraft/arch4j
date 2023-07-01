package org.oopscraft.arch4j.core.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.page.dao.PageEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class PageServiceTest extends CoreTestSupport {

    final PageService pageService;

    Page testPage = Page.builder()
            .pageId("test_id")
            .content("test_content")
            .build();

    @Test
    @Order(1)
    void savePage() {
        Page savedPage = pageService.savePage(testPage);
        assertNotNull(savedPage);
        assertNotNull(entityManager.find(PageEntity.class, testPage.getPageId()));
    }

    @Test
    @Order(2)
    void getPage() {
        Page savedPage = pageService.savePage(testPage);
        Page page = pageService.getPage(savedPage.getPageId()).orElse(null);
        assertNotNull(page);
        assertEquals(savedPage.getPageId(), page.getPageId());
    }

    @Test
    @Order(3)
    void deletePage() {
        Page savedPage = pageService.savePage(testPage);
        pageService.deletePage(savedPage.getPageId());
        assertNull(entityManager.find(PageEntity.class, testPage.getPageId()));
    }

    @Test
    @Order(4)
    void getPages() {
        Page savedPage = pageService.savePage(testPage);
        PageSearch pageSearch = PageSearch.builder()
                .pageId(savedPage.getPageId())
                .build();
        org.springframework.data.domain.Page<Page> codePage = pageService.getPages(pageSearch, PageRequest.of(0,10));
        assertTrue(codePage.getContent().stream().anyMatch(e -> e.getPageId().contains(pageSearch.getPageId())));
    }

}

