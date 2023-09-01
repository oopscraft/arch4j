package org.oopscraft.arch4j.core.user.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserRepositoryTest extends CoreTestSupport {

    private final UserRepository userRepository;

    private UserEntity getTestUserEntity() {
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .userName("Test")
                .build();
        Arrays.asList("role-1","role-2").forEach(roleId -> {
            entityManager.persist(RoleEntity.builder()
                    .roleId(roleId)
                    .roleName("name of " + roleId)
                    .build());
            entityManager.flush();
            userEntity.getUserRoleEntities().add(UserRoleEntity.builder()
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
    @Order(1)
    void saveToPersist() {
        // given
        UserEntity testUserEntity = getTestUserEntity();

        // when
        userRepository.saveAndFlush(testUserEntity);

        // then
        entityManager.flush();
        entityManager.clear();
        assertNotNull(entityManager.find(UserEntity.class, testUserEntity.getUserId()));
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        UserEntity testUserEntity = saveTestUserEntity();

        // when
        testUserEntity.setUserName("changed");
        userRepository.saveAndFlush(testUserEntity);

        // then
        entityManager.flush();
        entityManager.clear();
        assertEquals(
                "changed",
                entityManager.find(UserEntity.class, testUserEntity.getUserId()).getUserName()
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