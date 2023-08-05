package org.oopscraft.arch4j.core.user.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserRepositoryTest extends CoreTestSupport {

    private final UserRepository userRepository;

    private UserEntity getTestUserEntity() {
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .userName("Test")
                .build();
        Arrays.asList("ADMIN","invalid").forEach(roleId -> {
            userEntity.getUserRoleEntities().add(UserRoleEntity.builder()
                    .userId(userEntity.getUserId())
                    .roleId(roleId)
                    .build());
        });
        return userEntity;
    }

    private UserEntity createTestUserEntity() {
        UserEntity userEntity = getTestUserEntity();
        entityManager.persist(userEntity);
        entityManager.flush();
        entityManager.clear();
        return userEntity;
    }

    @Test
    @Order(1)
    void savePersist() {
        // given
        UserEntity userEntity = getTestUserEntity();

        // when
        userRepository.saveAndFlush(userEntity);

        // then
        assertNotNull(entityManager.find(UserEntity.class, userEntity.getUserId()));
    }

    @Test
    @Order(2)
    void saveMerge() {
        // given
        UserEntity userEntity = createTestUserEntity();

        // when
        userEntity.setUserName("changed user name");
        userRepository.saveAndFlush(userEntity);
        entityManager.clear();

        // then
        assertEquals("changed user name", entityManager.find(UserEntity.class, userEntity.getUserId()).getUserName());
    }

    @Test
    @Order(3)
    void saveMergeWithChildEntity() {
        // given
        UserEntity userEntity = createTestUserEntity();

        // when
        userEntity.getUserRoleEntities().clear();
        userEntity.getUserRoleEntities().add(UserRoleEntity.builder()
                .userId(userEntity.getUserId())
                .roleId("test-role")
                .build());
        userRepository.saveAndFlush(userEntity);
        entityManager.clear();

        // then
        UserEntity savedUserEntity = entityManager.find(UserEntity.class, userEntity.getUserId());
        assertNotNull(savedUserEntity);
        assertEquals("test-role", savedUserEntity.getUserRoleEntities().get(0).getRoleId());
    }

    @Test
    @Order(4)
    void delete() {
        // given
        UserEntity userEntity = createTestUserEntity();

        // when
        userRepository.deleteById(userEntity.getUserId());
        userRepository.flush();
        entityManager.clear();

        // then
        assertNull(entityManager.find(UserEntity.class, userEntity.getUserId()));
    }


}