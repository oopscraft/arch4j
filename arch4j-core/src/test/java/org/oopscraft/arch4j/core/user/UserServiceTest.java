package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.oopscraft.arch4j.core.user.repository.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserServiceTest extends CoreTestSupport {

    final UserService userService;

    User testUser = User.builder()
            .userId("test_user")
            .password("password1234!@#$")
            .name("user name")
            .mobile("010-1234-5678")
            .email("lion@xxx.com")
            .profile("profile")
            .build();

    @Test
    @Order(1)
    void saveUser() {
        User savedUser = userService.saveUser(testUser);
        assertNotNull(savedUser);
        assertNotNull(entityManager.find(UserEntity.class, testUser.getUserId()));
    }

    @Test
    @Order(2)
    void getUser() {
        User savedUser = userService.saveUser(testUser);
        User user = userService.getUser(savedUser.getUserId()).orElse(null);
        assertNotNull(user);
    }

    @Test
    @Order(3)
    void deleteUser() {
        User savedUser = userService.saveUser(testUser);
        userService.deleteUser(savedUser.getUserId());
        assertNull(entityManager.find(UserEntity.class, testUser.getUserId()));
    }

    @Test
    @Order(4)
    void getUsers() {
        User savedUser = userService.saveUser(testUser);
        UserSearch userSearch = UserSearch.builder()
                .name(savedUser.getName())
                .build();
        Page<User> userPage = userService.getUsers(userSearch, PageRequest.of(0, 10));
        assertTrue(userPage.getContent().stream().anyMatch(e -> e.getName().contains(userSearch.getName())));
    }

}