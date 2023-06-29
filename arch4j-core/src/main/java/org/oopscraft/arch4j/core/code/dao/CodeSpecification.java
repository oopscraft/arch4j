package org.oopscraft.arch4j.core.code.dao;

import org.springframework.data.jpa.domain.Specification;

public class CodeSpecification {

    public static Specification<CodeEntity> likeCodeId(String codeId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(CodeEntity_.CODE_ID), '%' + codeId + '%');
    }

    public static Specification<CodeEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(CodeEntity_.NAME), '%' + name + '%');
    }

}
