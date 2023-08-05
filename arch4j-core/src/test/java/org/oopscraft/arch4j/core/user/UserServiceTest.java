package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.oopscraft.arch4j.core.user.dao.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserServiceTest extends CoreTestSupport {

    private final UserService userService;

    private User getTestUser() {
        User user = User.builder()
                .userId("test_user")
                .password("password1234!@#$")
                .userName("user name")
                .mobile("010-1234-5678")
                .email("lion@xxx.com")
                .profile("profile")
                .build();
        IntStream.range(0,3)
                .mapToObj(i -> Role.builder()
                        .roleId("test-role" + i)
                        .build())
                .forEach(role -> {
                    user.getRoles().add(role);
                });
        return user;
    }

    private User createTestUser() {
        User user = getTestUser();
        userService.saveUser(user);
        entityManager.clear();
        return user;
    }


    @Test
    @Order(1)
    void saveUser() {
        // given
        User testUser = getTestUser();

        // when
        User user = userService.saveUser(testUser);

        // then
        assertNotNull(entityManager.find(UserEntity.class, testUser.getUserId()));
    }

    @Test
    @Order(2)
    void getUser() {
        // given
        User testUser = createTestUser();

        // when
        User user = userService.getUser(testUser.getUserId()).orElse(null);

        // then
        assertNotNull(user);
    }

    @Test
    @Order(3)
    void deleteUser() {
        // given
        User testUser = createTestUser();

        // when
        userService.deleteUser(testUser.getUserId());

        // then
        assertNull(entityManager.find(UserEntity.class, testUser.getUserId()));
    }

    @Test
    @Order(4)
    void getUsers() {

        // given
        User testUser = createTestUser();

        // when
        UserSearch userSearch = UserSearch.builder()
                .userName(testUser.getUserName())
                .build();
        Page<User> userPage = userService.getUsers(userSearch, PageRequest.of(0, 10));

        // then
        assertTrue(userPage.getContent().stream().anyMatch(e -> e.getUserName().contains(userSearch.getUserName())));
    }

}