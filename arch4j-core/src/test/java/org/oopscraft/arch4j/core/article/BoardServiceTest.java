package org.oopscraft.arch4j.core.article;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.article.repository.BoardEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserSearch;
import org.oopscraft.arch4j.core.user.repository.UserEntity;
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
        boardService.saveBoard(testBoard);
        assertNotNull(entityManager.find(BoardEntity.class, testBoard.getId()));
    }

    @Test
    @Order(2)
    void getUser() {

        // save test board
        saveBoard();

        // get board
        Board board = boardService.getBoard(testBoard.getId()).orElse(null);

        // check
        assertNotNull(board);
    }

    @Test
    @Order(3)
    void deleteBoard() {

        // save test board
        saveBoard();

        // delete
        boardService.deleteBoard(testBoard.getId());

        // check
        assertNull(entityManager.find(BoardEntity.class, testBoard.getId()));
    }

    @Test
    @Order(4)
    void getUsers() {

        // save test user
        saveBoard();

        // get board by condition
        BoardSearch boardSearch = BoardSearch.builder()
                .name(testBoard.getName())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> boardPage = boardService.getBoards(boardSearch, pageable);

        // check result
        assertTrue(boardPage.getContent().stream().anyMatch(e -> e.getName().contains(boardSearch.getName())));
    }

}