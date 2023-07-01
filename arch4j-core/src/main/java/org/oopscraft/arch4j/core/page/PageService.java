package org.oopscraft.arch4j.core.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.page.dao.PageEntity;
import org.oopscraft.arch4j.core.page.dao.PageWidgetEntity;
import org.oopscraft.arch4j.core.page.dao.PageRepository;
import org.oopscraft.arch4j.core.page.dao.PageSpecification;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PageService {

    private final PageRepository pageRepository;

    private final ApplicationContext applicationContext;

    /**
     * save page
     * @param page home panel
     * @return saved home panel
     */
    public Page savePage(Page page) {

        // get page
        PageEntity pageEntity = Optional.ofNullable(page.getPageId())
                .flatMap(pageRepository::findById)
                .orElse(null);

        // create new page
        if(pageEntity == null) {
            pageEntity = PageEntity.builder()
                    .pageId(page.getPageId())
                    .build();
        }

        // set properties
        pageEntity.setName(page.getName());
        pageEntity.setContentFormat(page.getContentFormat());
        pageEntity.setContent(page.getContent());

        // update panels
        pageEntity.getWidgets().clear();
        int index = 0;
        for(PageWidget pageWidget : page.getWidgets()) {
            index ++;
            pageEntity.getWidgets().add(PageWidgetEntity.builder()
                    .pageId(page.getPageId())
                    .index(index)
                    .type(pageWidget.getType())
                    .properties(pageWidget.getProperties())
                    .build());
        }

        // save
        pageEntity = pageRepository.saveAndFlush(pageEntity);
        return Page.from(pageEntity);
    }

    /**
     * search pages
     * @param pageSearch search condition
     * @param pageable pageable
     * @return page list
     */
    public PageImpl<Page> getPages(PageSearch pageSearch, Pageable pageable) {

        Specification<PageEntity> specification = (root, query, criteriaBuilder) -> null;
        if(pageSearch.getPageId() != null) {
            specification = specification.and(PageSpecification.likePageId(pageSearch.getPageId()));
        }
        if(pageSearch.getName() != null) {
            specification = specification.and(PageSpecification.likeName(pageSearch.getName()));
        }

        org.springframework.data.domain.Page<PageEntity> pageEntityPage = pageRepository.findAll(specification, pageable);
        List<Page> pages = pageEntityPage.getContent().stream()
                .map(Page::from)
                .collect(Collectors.toList());
        fillUrl(pages);
        long total = pageEntityPage.getTotalElements();
        return new PageImpl<>(pages, pageable, total);
    }

    /**
     * return page
     * @param pageId page id
     * @return page
     */
    public Optional<Page> getPage(String pageId) {
        Page page = pageRepository.findById(pageId).map(Page::from).orElseThrow();
        fillUrl(page);
        return Optional.of(page);
    }

    private void fillUrl(final Page page) {
        page.getWidgets().forEach(pageWidget -> {
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

    /**
     * delete page
     * @param pageId page id
     */
    public void deletePage(String pageId) {
        pageRepository.deleteById(pageId);
    }

}
