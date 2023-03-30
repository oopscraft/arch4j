package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.ServiceTestSupport;
import org.oopscraft.arch4j.core.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserServiceTest extends ServiceTestSupport {

    final UserService userService;

    User testUser = User.builder()
            .id(UUID.randomUUID().toString())
            .name("user name")
            .nickname("user nickname")
            .mobile("010-1234-5678")
            .email("lion@xxx.com")
            .profile("profile")
            .build();

    @Test
    void saveUser() {
        userService.saveUser(testUser);
        assertNotNull(entityManager.find(UserEntity.class, testUser.getId()));
    }

    @Test
    void getUsers() {

        // save test user
        saveUser();

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

        // save test user
        saveUser();

        // get user
        User user = userService.getUser(testUser.getId()).orElse(null);

        // check
        assertNotNull(user);
    }

    @Test
    void deleteUser() {

        // save test user
        saveUser();

        // delete
        userService.deleteUser(testUser.getId());

        // check
        assertNull(entityManager.find(UserEntity.class, testUser.getId()));
    }

}