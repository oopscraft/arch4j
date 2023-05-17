package org.oopscraft.arch4j.core.message.repository;

import org.oopscraft.arch4j.core.menu.repository.MenuEntity_;
import org.springframework.data.jpa.domain.Specification;

public class MessageSpecification {

    public static Specification<MessageEntity> likeId(String id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(MessageEntity_.ID), '%' + id + '%');
    }

    public static Specification<MessageEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(MessageEntity_.NAME), '%' + name + '%');
    }

}
