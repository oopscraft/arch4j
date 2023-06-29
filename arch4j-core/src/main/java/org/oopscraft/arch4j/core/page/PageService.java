package org.oopscraft.arch4j.core.page;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.page.dao.PageEntity;
import org.oopscraft.arch4j.core.page.dao.PagePanelEntity;
import org.oopscraft.arch4j.core.page.dao.PageRepository;
import org.oopscraft.arch4j.core.page.dao.PageSpecification;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;

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
        pageEntity.getPanels().clear();
        for(PagePanel pagePanel : page.getPanels()) {
            pageEntity.getPanels().add(PagePanelEntity.builder()
                    .pageId(page.getPageId())
                    .index(pagePanel.getIndex())
                    .type(pagePanel.getType())
                    .properties(pagePanel.getProperties())
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
        long total = pageEntityPage.getTotalElements();
        return new PageImpl<>(pages, pageable, total);
    }

    /**
     * return page
     * @param pageId page id
     * @return page
     */
    public Optional<Page> getPage(String pageId) {
        return pageRepository.findById(pageId).map(Page::from);
    }

    /**
     * delete page
     * @param pageId page id
     */
    public void deletePage(String pageId) {
        pageRepository.deleteById(pageId);
    }

}
