package org.oopscraft.arch4j.core.security.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.security.model.User;
import org.oopscraft.arch4j.core.security.model.UserSearch;
import org.oopscraft.arch4j.core.common.test.CoreTestSupport;
import org.oopscraft.arch4j.core.security.dao.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserServiceTest extends CoreTestSupport {

    private final UserService userService;

    @Test
    void saveUserToPersist() {
        // given
        User user = User.builder()
                .username("username")
                .password("password1234!@#$")
                .name("test user")
                .build();
        // when
        User savedUser = userService.saveUser(user);
        // then
        assertNotNull(entityManager.find(UserEntity.class, savedUser.getUserId()));
    }

    @Test
    void saveUserToMerge() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("1234")
                .username("username")
                .password("password1234!@#$")
                .name("test user")
                .build();
        entityManager.persist(userEntity);
        entityManager.flush();
        // when
        User savedUser = userService.getUser(userEntity.getUserId()).orElseThrow();
        // then
        assertNotNull(entityManager.find(UserEntity.class, savedUser.getUserId()));
    }

    @Test
    void getUser() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("1234")
                .username("username")
                .password("password1234!@#$")
                .name("test user")
                .build();
        entityManager.persist(userEntity);
        entityManager.flush();
        // when
        User user = userService.getUser(userEntity.getUserId()).orElse(null);
        // then
        assertNotNull(user);
    }

    @Test
    void deleteUser() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("1234")
                .username("username")
                .password("password1234!@#$")
                .name("test user")
                .build();
        entityManager.persist(userEntity);
        entityManager.flush();
        // when
        userService.deleteUser(userEntity.getUserId());
        // then
        assertNull(entityManager.find(UserEntity.class, userEntity.getUserId()));
    }

    @Test
    void getUsers() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("1234")
                .username("username")
                .password("password1234!@#$")
                .name("test user")
                .build();
        entityManager.persist(userEntity);
        entityManager.flush();
        // when
        UserSearch userSearch = UserSearch.builder()
                .username(userEntity.getUsername())
                .build();
        Page<User> userPage = userService.getUsers(userSearch, PageRequest.of(0, 10));
        // then
        assertTrue(userPage.getContent().stream()
                .anyMatch(e ->
                        e.getUsername().contains(userSearch.getUsername())
                )
        );
    }

}