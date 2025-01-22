package org.chomookun.arch4j.core.page.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.page.dao.PageEntity;
import org.chomookun.arch4j.core.page.model.Page;
import org.chomookun.arch4j.core.page.widget.PageWidget;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class PageServiceTest extends CoreTestSupport {

    private final PageService pageService;

    private Page getTestPage() {
        Page testPage = Page.builder()
                .pageId("test_id")
                .content("test_content")
                .build();

        IntStream.range(0,3).forEach(index -> {
            testPage.getPageWidgets().add(PageWidget.builder()
                    .pageId(testPage.getPageId())
                    .index(index)
                    .type(this.getClass().getName())
                    .properties("name=value")
                    .build());
        });

        return testPage;
    }

    private Page saveTestPage() {
        Page page = getTestPage();
        pageService.savePage(page);
        entityManager.clear();
        return page;
    }

    @Test
    @Order(1)
    void savePageToPersist() {
        // given
        Page testPage = getTestPage();

        // when
        Page savedPage = pageService.savePage(testPage);

        // then
        assertNotNull(entityManager.find(PageEntity.class, savedPage.getPageId()));
    }

    @Test
    @Order(2)
    void savePageToMerge() {
        // given
        Page testPage = saveTestPage();

        // when
        testPage.setName("changed");
        Page page = pageService.savePage(testPage);

        // then
        entityManager.clear();
        assertEquals(
                "changed",
                entityManager.find(PageEntity.class,page.getPageId()).getName()
        );
    }

    @Test
    @Order(3)
    void getPage() {
        // given
        Page testPage = saveTestPage();

        // when
        Page page = pageService.getPage(testPage.getPageId()).orElseThrow();

        // then
        assertEquals(testPage.getPageId(), page.getPageId());
    }

    @Test
    @Order(3)
    void deletePage() {
        // given
        Page testPage = saveTestPage();

        // when
        pageService.deletePage(testPage.getPageId());

        // then
        entityManager.clear();
        assertNull(entityManager.find(PageEntity.class, testPage.getPageId()));
    }

}

