package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.board.repository.BoardEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class BoardServiceTest extends CoreTestSupport {

    final BoardService boardService;

    Board testBoard = Board.builder()
            .id(UUID.randomUUID().toString())
            .name("test board")
            .build();

    @Test
    @Order(1)
    void saveBoard() {
        Board savedBoard = boardService.saveBoard(testBoard);
        assertNotNull(entityManager.find(BoardEntity.class, savedBoard.getId()));
    }

    @Test
    @Order(2)
    void getUser() {
        Board savedBoard = boardService.saveBoard(testBoard);
        Board board = boardService.getBoard(savedBoard.getId()).orElse(null);
        assertNotNull(board);
    }

    @Test
    @Order(3)
    void deleteBoard() {
        Board savedBoard = boardService.saveBoard(testBoard);
        boardService.deleteBoard(savedBoard.getId());
        assertNull(entityManager.find(BoardEntity.class, savedBoard.getId()));
    }

    @Test
    @Order(4)
    void getBoard() {
        Board savedBoard = boardService.saveBoard(testBoard);
        BoardSearch boardSearch = BoardSearch.builder()
                .name(savedBoard.getName())
                .build();
        Page<Board> boardPage = boardService.getBoards(boardSearch, PageRequest.of(0,10));
        assertTrue(boardPage.getContent().stream().anyMatch(e -> e.getName().contains(boardSearch.getName())));
    }

}