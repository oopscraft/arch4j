package org.oopscraft.arch4j.core.page.dao;

import org.oopscraft.arch4j.core.code.dao.CodeEntity;
import org.oopscraft.arch4j.core.code.dao.CodeEntity_;
import org.springframework.data.jpa.domain.Specification;

public class PageSpecification {

    public static Specification<PageEntity> likePageId(String pageId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(PageEntity_.PAGE_ID), '%' + pageId + '%');
    }

    public static Specification<PageEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(PageEntity_.NAME), '%' + name + '%');
    }

}
