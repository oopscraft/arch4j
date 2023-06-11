package org.oopscraft.arch4j.core.message.repository;

import org.springframework.data.jpa.domain.Specification;

public class MessageSpecification {

    public static Specification<MessageEntity> likeMessageId(String messageId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(MessageEntity_.MESSAGE_ID), '%' + messageId + '%');
    }

    public static Specification<MessageEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(MessageEntity_.NAME), '%' + name + '%');
    }

}