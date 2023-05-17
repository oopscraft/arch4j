package org.oopscraft.arch4j.core.code.repository;

import org.oopscraft.arch4j.core.menu.repository.MenuEntity_;
import org.springframework.data.jpa.domain.Specification;

public class CodeSpecification {

    public static Specification<CodeEntity> likeId(String id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(CodeEntity_.ID), '%' + id + '%');
    }

    public static Specification<CodeEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(CodeEntity_.NAME), '%' + name + '%');
    }

}
