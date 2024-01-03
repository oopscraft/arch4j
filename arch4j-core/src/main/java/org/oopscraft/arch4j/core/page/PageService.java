package org.oopscraft.arch4j.core.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.page.dao.PageEntity;
import org.oopscraft.arch4j.core.page.dao.PageRepository;
import org.oopscraft.arch4j.core.page.dao.PageWidgetEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PageService {

    private final PageRepository pageRepository;

    private final ApplicationContext applicationContext;

    @Transactional
    public Page savePage(Page page) {
        PageEntity pageEntity = Optional.ofNullable(page.getPageId())
                .flatMap(pageRepository::findById)
                .orElse(PageEntity.builder()
                        .pageId(page.getPageId())
                        .build());
        pageEntity.setPageName(page.getPageName());
        pageEntity.setContentFormat(page.getContentFormat());
        pageEntity.setContent(page.getContent());
        pageEntity.getPageWidgets().clear();

        // widget
        int index = 0;
        for(PageWidget pageWidget : page.getPageWidgets()) {
            index ++;
            pageEntity.getPageWidgets().add(PageWidgetEntity.builder()
                    .pageId(page.getPageId())
                    .index(index)
                    .type(pageWidget.getType())
                    .properties(pageWidget.getProperties())
                    .build());
        }

        // save and return
        pageEntity = pageRepository.saveAndFlush(pageEntity);
        return Page.from(pageEntity);
    }

    public PageImpl<Page> getPages(PageSearch pageSearch, Pageable pageable) {
        org.springframework.data.domain.Page<PageEntity> pageEntityPage = pageRepository.findAll(pageSearch, pageable);
        List<Page> pages = pageEntityPage.getContent().stream()
                .map(Page::from)
                .collect(Collectors.toList());
        fillUrl(pages);
        long total = pageEntityPage.getTotalElements();
        return new PageImpl<>(pages, pageable, total);
    }

    public Optional<Page> getPage(String pageId) {
        Page page = pageRepository.findById(pageId).map(Page::from).orElseThrow();
        fillUrl(page);
        return Optional.of(page);
    }

    private void fillUrl(final Page page) {
        page.getPageWidgets().forEach(pageWidget -> {
            pageWidget.setUrl(getPageWidgetUrl(pageWidget));
        });
    }

    private void fillUrl(final List<Page> pages) {
        pages.forEach(this::fillUrl);
    }

    public List<PageWidgetDefinition> getPageWidgetDefinitions() {
        List<PageWidgetDefinition> pageWidgetDefinitions = new ArrayList<>();
        Map<String, PageWidgetAware> beansMap = applicationContext.getBeansOfType(PageWidgetAware.class);
        for(PageWidgetAware bean : beansMap.values()){
            pageWidgetDefinitions.add(bean.getDefinition());
        }
        return pageWidgetDefinitions;
    }

    public String getPageWidgetUrl(PageWidget pageWidget) {
        try {
            Class<?> typeClass = Class.forName(pageWidget.getType());
            Properties properties = new Properties();
            properties.load(new StringReader(pageWidget.getProperties()));
            PageWidgetAware bean = (PageWidgetAware) applicationContext.getBean(typeClass);
            return bean.getUrl(properties);
        }catch(Exception e){
            log.warn(e.getMessage());
            return null;
        }
    }

    @Transactional
    public void deletePage(String pageId) {
        pageRepository.deleteById(pageId);
        pageRepository.flush();
    }

}
