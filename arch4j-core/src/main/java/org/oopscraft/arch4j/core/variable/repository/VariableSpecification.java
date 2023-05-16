package org.oopscraft.arch4j.core.variable.repository;

import org.springframework.data.jpa.domain.Specification;

public class VariableSpecification {

    public static Specification<VariableEntity> likeId(String id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(VariableEntity_.ID), '%' + id + '%');
    }

    public static Specification<VariableEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
               criteriaBuilder.like(root.get(VariableEntity_.NAME), '%' + name + '%');
    }

}
