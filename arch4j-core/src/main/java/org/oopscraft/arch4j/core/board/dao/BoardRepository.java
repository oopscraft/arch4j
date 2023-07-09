package org.oopscraft.arch4j.core.board.dao;

import org.oopscraft.arch4j.core.board.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, String>, JpaSpecificationExecutor<BoardEntity> {

    default Page<BoardEntity> findAll(BoardSearch boardSearch, Pageable pageable) {
        Specification<BoardEntity> specification = (root, query, criteriaBuilder) -> null;
        if(boardSearch.getBoardId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(BoardEntity_.BOARD_ID), '%' + boardSearch.getBoardId() + '%'));
        }
        if(boardSearch.getBoardName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(BoardEntity_.BOARD_NAME), '%' + boardSearch.getBoardName() + '%'));
        }
        return findAll(specification, pageable);
    }

}
