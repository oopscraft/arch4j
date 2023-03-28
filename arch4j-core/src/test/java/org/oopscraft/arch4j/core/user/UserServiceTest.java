package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.ServiceTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserServiceTest extends ServiceTestSupport {

    final UserService userService;

    User createTestUser() {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .name("user name")
                .nickname("user nickname")
                .build();
    }

    @Test
    void saveUser() {
        User testUser = createTestUser();
        userService.saveUser(testUser);
        assertNotNull(entityManager.find(User.class, testUser.getId()));
    }

    @Test
    void getUsers() {

        // save test user
        User testUser = createTestUser();
        entityManager.persist(testUser);

        // get users by condition
        UserSearch userSearch = UserSearch.builder()
                .name(testUser.getName())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = userService.getUsers(userSearch, pageable);

        // check result
        assertTrue(userPage.getContent().stream().anyMatch(e -> e.getName().contains(userSearch.getName())));
    }

    @Test
    void getUser() {

        // save test
        User testUser = createTestUser();
        entityManager.persist(testUser);

        // get user
        User user = userService.getUser(testUser.getId()).orElse(null);

        // check
        assertNotNull(user);
    }

    @Test
    void deleteUser() {

        // save test user
        User testUser = createTestUser();
        entityManager.persist(testUser);

        // delete
        userService.deleteUser(testUser.getId());

        // check
        assertNull(entityManager.find(User.class, testUser.getId()));
    }

}