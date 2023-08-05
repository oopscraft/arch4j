package org.oopscraft.arch4j.core.board.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class BoardRepositoryTest extends CoreTestSupport {

    private final BoardRepository boardRepository;

    private BoardEntity getTestBoardEntity() {
        BoardEntity boardEntity = BoardEntity.builder()
                .boardId("test-board")
                .boardName("Test board")
                .build();
        Arrays.asList("ADMIN","invalid").forEach(roleId -> {
            boardEntity.getReadBoardRoleEntities().add(BoardRoleEntity.builder()
                    .boardId(boardEntity.getBoardId())
                    .roleId(roleId)
                    .type("READ")
                    .build());
            boardEntity.getWriteBoardRoleEntities().add(BoardRoleEntity.builder()
                    .boardId(boardEntity.getBoardId())
                    .roleId(roleId)
                    .type("WRITE")
                    .build());
            boardEntity.getCommentBoardRoleEntities().add(BoardRoleEntity.builder()
                    .boardId(boardEntity.getBoardId())
                    .roleId(roleId)
                    .type("COMMENT")
                    .build());
        });
        return boardEntity;
    }

    private BoardEntity createTestBoardEntity() {
        BoardEntity testBoardEntity = getTestBoardEntity();
        entityManager.persist(testBoardEntity);
        entityManager.flush();
        entityManager.clear();
        return testBoardEntity;
    }

    @Test
    @Order(1)
    void saveToPersist() {
        // given
        BoardEntity testBoardEntity = getTestBoardEntity();

        // when
        BoardEntity boardEntity = boardRepository.saveAndFlush(testBoardEntity);

        // then
        BoardEntity savedBoardEntity = entityManager.find(BoardEntity.class, boardEntity.getBoardId());
        assertNotNull(savedBoardEntity);
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        BoardEntity testBoardEntity = createTestBoardEntity();

        // when
        testBoardEntity.setBoardName("changed board name");
        BoardEntity boardEntity = boardRepository.saveAndFlush(testBoardEntity);

        // then
        BoardEntity savedBoardEntity = entityManager.find(BoardEntity.class, boardEntity.getBoardId());
        assertNotNull(savedBoardEntity);
        assertEquals("changed board name", savedBoardEntity.getBoardName());
    }

    @Test
    @Order(3)
    void delete() {
        // given
        BoardEntity testBoardEntity = createTestBoardEntity();

        // when
        boardRepository.deleteById(testBoardEntity.getBoardId());

        // then
        BoardEntity deletedBoardEntity = entityManager.find(BoardEntity.class, testBoardEntity.getBoardId());
        assertNull(deletedBoardEntity);
    }

}