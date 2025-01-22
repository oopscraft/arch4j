package org.chomookun.arch4j.core.board.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.chomookun.arch4j.core.security.dao.RoleEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class BoardRepositoryTest extends CoreTestSupport {

    private final BoardRepository boardRepository;

    private BoardEntity getTestBoardEntity() {
        BoardEntity boardEntity = BoardEntity.builder()
                .boardId("test-board")
                .name("Test board")
                .build();
        Arrays.asList("role-1","role-2").forEach(roleId -> {
            entityManager.persist(RoleEntity.builder()
                    .roleId(roleId)
                    .name("name of " + roleId)
                    .build());
            entityManager.flush();
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
        testBoardEntity.setName("changed board name");
        BoardEntity boardEntity = boardRepository.saveAndFlush(testBoardEntity);

        // then
        BoardEntity savedBoardEntity = entityManager.find(BoardEntity.class, boardEntity.getBoardId());
        assertNotNull(savedBoardEntity);
        assertEquals("changed board name", savedBoardEntity.getName());
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