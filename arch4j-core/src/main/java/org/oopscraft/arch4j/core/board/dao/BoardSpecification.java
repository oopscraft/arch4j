package org.oopscraft.arch4j.core.board.dao;

import org.springframework.data.jpa.domain.Specification;

public class BoardSpecification {

    /**
     * linkId
     * @param boardId board id
     * @return specification
     */
    public static Specification<BoardEntity> likeBoardId(String boardId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(BoardEntity_.BOARD_ID), '%' + boardId + '%');
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
