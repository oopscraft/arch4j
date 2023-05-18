package org.oopscraft.arch4j.core.article.repository;

import org.springframework.data.jpa.domain.Specification;

public class BoardSpecification {

    /**
     * linkId
     * @param id board id
     * @return specification
     */
    public static Specification<BoardEntity> likeId(String id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(BoardEntity_.ID), '%' + id + '%');
    }

    /**
     * likeName
     * @param name board name
     * @return specification
     */
    public static Specification<BoardEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(BoardEntity_.NAME), '%' + name + '%');
    }

}
