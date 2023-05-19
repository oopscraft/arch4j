package org.oopscraft.arch4j.core.role.repository;

import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {

    public static Specification<RoleEntity> likeId(String id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(RoleEntity_.ID), '%' + id + '%');
    }

    public static Specification<RoleEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
               criteriaBuilder.like(root.get(RoleEntity_.NAME), '%' + name + '%');
    }

}
