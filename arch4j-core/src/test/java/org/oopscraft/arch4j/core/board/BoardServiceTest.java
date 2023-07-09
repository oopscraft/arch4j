package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.board.dao.BoardEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class BoardServiceTest extends CoreTestSupport {

    final BoardService boardService;

    Board testBoard = Board.builder()
            .boardId(UUID.randomUUID().toString())
            .boardName("test board")
            .build();

    @Test
    @Order(1)
    void saveBoard() {
        Board savedBoard = boardService.saveBoard(testBoard);
        assertNotNull(entityManager.find(BoardEntity.class, savedBoard.getBoardId()));
    }

    @Test
    @Order(2)
    void getUser() {
        Board savedBoard = boardService.saveBoard(testBoard);
        Board board = boardService.getBoard(savedBoard.getBoardId()).orElse(null);
        assertNotNull(board);
    }

    @Test
    @Order(3)
    void deleteBoard() {
        Board savedBoard = boardService.saveBoard(testBoard);
        boardService.deleteBoard(savedBoard.getBoardId());
        assertNull(entityManager.find(BoardEntity.class, savedBoard.getBoardId()));
    }

    @Test
    @Order(4)
    void getBoard() {
        Board savedBoard = boardService.saveBoard(testBoard);
        BoardSearch boardSearch = BoardSearch.builder()
                .boardName(savedBoard.getBoardName())
                .build();
        Page<Board> boardPage = boardService.getBoards(boardSearch, PageRequest.of(0,10));
        assertTrue(boardPage.getContent().stream().anyMatch(e -> e.getBoardName().contains(boardSearch.getBoardName())));
    }

}