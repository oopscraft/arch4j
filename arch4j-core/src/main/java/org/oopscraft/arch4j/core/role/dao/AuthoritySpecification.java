package org.oopscraft.arch4j.core.role.dao;

import org.springframework.data.jpa.domain.Specification;

public class AuthoritySpecification {

    public static Specification<AuthorityEntity> likeAuthorityId(String id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(AuthorityEntity_.AUTHORITY_ID), '%' + id + '%');
    }

    public static Specification<AuthorityEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
               criteriaBuilder.like(root.get(AuthorityEntity_.NAME), '%' + name + '%');
    }

}
