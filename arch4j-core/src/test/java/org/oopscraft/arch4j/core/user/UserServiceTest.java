package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.support.CoreTestSupport;
import org.oopscraft.arch4j.core.user.dao.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

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

        Arrays.asList("role-1","role-2").forEach(roleId -> {
            entityManager.persist(RoleEntity.builder()
                    .roleId(roleId)
                    .roleName("name of " + roleId)
                    .build());
            entityManager.flush();
            user.getRoles().add(Role.builder()
                    .roleId(roleId)
                    .build());
        });

        return user;
    }

    private User saveTestUser() {
        User user = getTestUser();
        userService.saveUser(user);
        entityManager.clear();
        return user;
    }


    @Test
    @Order(1)
    void saveUserToPersist() {
        // given
        User testUser = getTestUser();

        // when
        User user = userService.saveUser(testUser);

        // then
        entityManager.flush();
        entityManager.clear();
        assertNotNull(entityManager.find(UserEntity.class, testUser.getUserId()));
    }

    @Test
    @Order(2)
    void saveUserToMerge() {
        // given
        User testUser = saveTestUser();

        // when
        testUser.setUserName("changed");
        userService.saveUser(testUser);

        // then
        entityManager.flush();
        entityManager.clear();
        assertEquals(
                "changed",
                entityManager.find(UserEntity.class, testUser.getUserId()).getUserName()
        );
    }

    @Test
    @Order(3)
    void getUser() {
        // given
        User testUser = saveTestUser();

        // when
        User user = userService.getUser(testUser.getUserId()).orElse(null);

        // then
        assertNotNull(user);
    }

    @Test
    @Order(4)
    void deleteUser() {
        // given
        User testUser = saveTestUser();

        // when
        userService.deleteUser(testUser.getUserId());

        // then
        assertNull(entityManager.find(UserEntity.class, testUser.getUserId()));
    }

    @Test
    @Order(5)
    void getUsers() {

        // given
        User testUser = saveTestUser();

        // when
        UserSearch userSearch = UserSearch.builder()
                .userName(testUser.getUserName())
                .build();
        Page<User> userPage = userService.getUsers(userSearch, PageRequest.of(0, 10));

        // then
        assertTrue(userPage.getContent().stream()
                .anyMatch(e ->
                        e.getUserName().contains(userSearch.getUserName())
                )
        );
    }

}