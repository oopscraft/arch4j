package org.oopscraft.arch4j.core.menu.dao;

import org.oopscraft.arch4j.core.menu.MenuSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity,String>, JpaSpecificationExecutor<MenuEntity> {

    default Page<MenuEntity> findAll(MenuSearch menuSearch, Pageable pageable) {
        Specification<MenuEntity> specification = (root, query, criteriaBuilder) -> null;
        if(menuSearch.getMenuId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(MenuEntity_.MENU_ID), '%' + menuSearch.getMenuId() + '%'));
        }
        if(menuSearch.getMenuName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(MenuEntity_.MENU_NAME), '%' + menuSearch.getMenuName() + '%'));
        }
        return findAll(specification, pageable);
    }

}
