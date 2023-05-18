package org.oopscraft.arch4j.core.article;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.article.repository.BoardEntity;
import org.oopscraft.arch4j.core.article.repository.BoardRepository;
import org.oopscraft.arch4j.core.article.repository.BoardSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * saves board info
     * @param board board info
     */
    public void saveBoard(Board board) {
        BoardEntity boardEntity = boardRepository.findById(board.getId()).orElse(null);
        if(boardEntity == null) {
            boardEntity = BoardEntity.builder()
                    .id(board.getId())
                    .build();
        }
        boardEntity.setName(board.getName());
        boardRepository.saveAndFlush(boardEntity);
    }

    /**
     * returns board
     * @param id board id
     * @return board info
     */
    public Optional<Board> getBoard(String id) {
        return boardRepository.findById(id)
                .map(Board::from);
    }

    /**
     * deletes board
     * @param id board id
     */
    public void deleteBoard(String id) {
        boardRepository.deleteById(id);
        boardRepository.flush();
    }

    /**
     * return list of board
     * @param boardSearch board search condition
     * @param pageable pagination info
     * @return board list
     */
    public Page<Board> getBoards(BoardSearch boardSearch, Pageable pageable) {
        Specification<BoardEntity> specification = (root, query, criteriaBuilder) -> null;
        if(boardSearch.getId() != null) {
            specification = specification.and(BoardSpecification.likeId(boardSearch.getId()));
        }
        if(boardSearch.getName() != null) {
            specification = specification.and(BoardSpecification.likeName(boardSearch.getName()));
        }
        Page<BoardEntity> boardEntityPage = boardRepository.findAll(specification, pageable);
        List<Board> boards = boardEntityPage.getContent().stream()
                .map(Board::from)
                .collect(Collectors.toList());
        long total = boardEntityPage.getTotalElements();
        return new PageImpl<>(boards, pageable, total);
    }


}
