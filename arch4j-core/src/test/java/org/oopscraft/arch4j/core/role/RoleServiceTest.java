package org.oopscraft.arch4j.core.role;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.role.dao.AuthorityEntity;
import org.oopscraft.arch4j.core.support.CoreTestSupport;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class RoleServiceTest extends CoreTestSupport {

    private final RoleService roleService;

    private Role getTestRole() {
        Role testRole = Role.builder()
                .roleId("role_id")
                .roleName("role_name")
                .build();

        // add authority
        Arrays.asList("auth-1","auto-2").forEach(authorityId -> {
            entityManager.persist(AuthorityEntity.builder()
                    .authorityId(authorityId)
                    .authorityName("name of " + authorityId)
                    .build());
            entityManager.flush();

            testRole.getAuthorities()
                    .add(Authority.builder()
                            .authorityId(authorityId)
                            .build());
        });

        return testRole;
    }

    private Role saveTestRole() {
        Role testRole = getTestRole();
        roleService.saveRole(testRole);
        return testRole;
    }

    @Test
    @Order(1)
    void saveRoleToPersist() {
        // given
        Role testRole = getTestRole();

        // when
        roleService.saveRole(testRole);

        // then
        assertNotNull(entityManager.find(RoleEntity.class, testRole.getRoleId()));
    }

    @Test
    @Order(2)
    void saveRoleToMerge() {
        // given
        Role testRole = saveTestRole();

        // when
        testRole.setRoleName("changed");
        roleService.saveRole(testRole);

        // then
        entityManager.clear();
        assertEquals(
                "changed",
                entityManager.find(RoleEntity.class, testRole.getRoleId()).getRoleName()
        );
    }

    @Test
    @Order(3)
    void getRole() {
        // given
        Role testRole = saveTestRole();

        // when
        Role role = roleService.getRole(testRole.getRoleId()).orElseThrow();

        // then
        assertNotNull(role);
    }

    @Test
    @Order(4)
    void deleteRole() {
        // given
        Role testRole = saveTestRole();

        // when
        roleService.deleteRole(testRole.getRoleId());

        // then
        RoleEntity roleEntity = entityManager.find(RoleEntity.class, testRole.getRoleId());
        assertNull(roleEntity);
    }

    @Test
    @Order(5)
    void getRoles() {
        // given
        Role testRole = saveTestRole();

        // when
        RoleSearch roleSearch = RoleSearch.builder()
                .roleName(testRole.getRoleName())
                .build();
        Page<Role> rolePage = roleService.getRoles(roleSearch, PageRequest.of(0,10));

        // then
        assertTrue(rolePage.getContent().stream()
                .anyMatch(e -> e.getRoleName().contains(roleSearch.getRoleName())));
    }

}