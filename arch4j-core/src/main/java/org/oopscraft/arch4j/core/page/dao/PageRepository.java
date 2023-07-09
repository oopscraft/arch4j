package org.oopscraft.arch4j.core.page.dao;

import org.oopscraft.arch4j.core.page.PageSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, String>, JpaSpecificationExecutor<PageEntity>  {

    default Page<PageEntity> findAll(PageSearch pageSearch, Pageable pageable) {
        Specification<PageEntity> specification = (root, query, criteriaBuilder) -> null;
        if(pageSearch.getPageId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(PageEntity_.PAGE_ID), '%' + pageSearch.getPageId() + '%'));
        }
        if(pageSearch.getPageName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(PageEntity_.PAGE_NAME), '%' + pageSearch.getPageName() + '%'));
        }
        return findAll(specification, pageable);
    }

}
