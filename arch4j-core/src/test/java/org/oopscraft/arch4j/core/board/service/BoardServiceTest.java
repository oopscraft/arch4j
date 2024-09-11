package org.oopscraft.arch4j.core.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.board.dao.BoardEntity;
import org.oopscraft.arch4j.core.board.model.Board;
import org.oopscraft.arch4j.core.board.service.BoardService;
import org.oopscraft.arch4j.core.security.model.Role;
import org.oopscraft.arch4j.core.common.test.CoreTestSupport;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class BoardServiceTest extends CoreTestSupport {

    final BoardService boardService;

    private Board getTestBoard() {
        Board testBoard = Board.builder()
                .boardId("test-board")
                .name("test board")
                .message("test message")
                .fileEnabled(true)
                .commentEnabled(true)
                .build();
        Arrays.asList("ADMIN","invalid").forEach(roleId -> {
            testBoard.getAccessRoles().add(Role.builder()
                    .roleId(roleId)
                    .build());
            testBoard.getReadRoles().add(Role.builder()
                    .roleId(roleId)
                    .build());
            testBoard.getWriteRoles().add(Role.builder()
                    .roleId(roleId)
                    .build());
            testBoard.getFileRoles().add(Role.builder()
                    .roleId(roleId)
                    .build());
            testBoard.getCommentRoles().add(Role.builder()
                    .roleId(roleId)
                    .build());
        });
        return testBoard;
    }

    private Board saveTestBoard() {
        Board testBoard = getTestBoard();
        boardService.saveBoard(testBoard);
        return testBoard;
    }

    @Test
    @Order(1)
    void saveBoard() {
        // given
        Board testBoard = getTestBoard();

        // when
        boardService.saveBoard(testBoard);

        // then
        BoardEntity boardEntity = entityManager.find(BoardEntity.class, testBoard.getBoardId());
        assertNotNull(boardEntity);
    }

    @Test
    @Order(2)
    void getBoard() {
        // given
        Board testBoard = saveTestBoard();

        // when
        Board board = boardService.getBoard(testBoard.getBoardId()).orElseThrow();

        // then
        assertNotNull(board);
    }

    @Test
    @Order(3)
    void deleteBoard() {
        // given
        Board testBoard = saveTestBoard();

        // when
        boardService.deleteBoard(testBoard.getBoardId());

        // then
        assertNull(entityManager.find(BoardEntity.class, testBoard.getBoardId()));
    }

}