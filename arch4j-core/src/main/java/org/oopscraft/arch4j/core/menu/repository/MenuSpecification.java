package org.oopscraft.arch4j.core.menu.repository;

import org.springframework.data.jpa.domain.Specification;

public class MenuSpecification {

    public static Specification<MenuEntity> likeId(String id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(MenuEntity_.ID), '%' + id + '%');
    }

    public static Specification<MenuEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(MenuEntity_.NAME), '%' + name + '%');
    }

}
