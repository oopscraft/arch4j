package org.oopscraft.arch4j.core.menu.dao;

import org.springframework.data.jpa.domain.Specification;

public class MenuSpecification {

    public static Specification<MenuEntity> likeMenuId(String menuId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(MenuEntity_.MENU_ID), '%' + menuId + '%');
    }

    public static Specification<MenuEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(MenuEntity_.NAME), '%' + name + '%');
    }

}
