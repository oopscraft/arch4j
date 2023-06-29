package org.oopscraft.arch4j.core.role.dao;

import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {

    public static Specification<RoleEntity> likeRoleId(String id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(RoleEntity_.ROLE_ID), '%' + id + '%');
    }

    public static Specification<RoleEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
               criteriaBuilder.like(root.get(RoleEntity_.NAME), '%' + name + '%');
    }

}
