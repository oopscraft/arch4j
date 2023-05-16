package org.oopscraft.arch4j.core.role.repository;

import org.oopscraft.arch4j.core.user.repository.AuthorityEntity_;
import org.springframework.data.jpa.domain.Specification;

public class AuthoritySpecification {

    public static Specification<AuthorityEntity> likeId(String id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(AuthorityEntity_.ID), '%' + id + '%');
    }

    public static Specification<AuthorityEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
               criteriaBuilder.like(root.get(AuthorityEntity_.NAME), '%' + name + '%');
    }

}
