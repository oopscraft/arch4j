package org.oopscraft.arch4j.core.security.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.common.test.CoreTestSupport;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserRepositoryTest extends CoreTestSupport {

    private final UserRepository userRepository;

    private UserEntity getTestUserEntity() {
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .username("Test")
                .build();
        Arrays.asList("role-1","role-2").forEach(roleId -> {
            entityManager.persist(RoleEntity.builder()
                    .roleId(roleId)
                    .name("name of " + roleId)
                    .build());
            entityManager.flush();
            userEntity.getUserRoles().add(UserRoleEntity.builder()
                    .userId(userEntity.getUserId())
                    .roleId(roleId)
                    .build());
        });
        return userEntity;
    }

    private UserEntity saveTestUserEntity() {
        UserEntity userEntity = getTestUserEntity();
        entityManager.persist(userEntity);
        entityManager.flush();
        entityManager.clear();
        return userEntity;
    }

    @Test
    void saveToPersist() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("test_user")
                .username("test-user")
                .build();
        // when
        userRepository.saveAndFlush(userEntity);

        // then
        assertNotNull(entityManager.find(UserEntity.class, userEntity.getUserId()));
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        UserEntity testUserEntity = saveTestUserEntity();

        // when
        testUserEntity.setUsername("changed");
        userRepository.saveAndFlush(testUserEntity);

        // then
        entityManager.flush();
        entityManager.clear();
        assertEquals(
                "changed",
                entityManager.find(UserEntity.class, testUserEntity.getUserId()).getUsername()
        );
    }

    @Test
    @Order(3)
    void findById() {
        // given
        UserEntity testUserEntity = saveTestUserEntity();

        // when
        UserEntity userEntity = userRepository.findById(testUserEntity.getUserId()).orElseThrow();

        // then
        assertEquals(testUserEntity.getUserId(), userEntity.getUserId());
    }

    @Test
    @Order(4)
    void deleteById() {
        // given
        UserEntity userEntity = saveTestUserEntity();

        // when
        userRepository.deleteById(userEntity.getUserId());

        // then
        userRepository.flush();
        entityManager.clear();
        assertNull(entityManager.find(UserEntity.class, userEntity.getUserId()));
    }

}