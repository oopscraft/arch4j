package org.chomookun.arch4j.core.security.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.security.dao.AuthorityEntity;
import org.chomookun.arch4j.core.security.model.Authority;
import org.chomookun.arch4j.core.security.model.Role;
import org.chomookun.arch4j.core.security.model.RoleSearch;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.chomookun.arch4j.core.security.dao.RoleEntity;
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
                .name("role_name")
                .build();

        // add authority
        Arrays.asList("auth-1","auto-2").forEach(authorityId -> {
            entityManager.persist(AuthorityEntity.builder()
                    .authorityId(authorityId)
                    .name("name of " + authorityId)
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
        testRole.setName("changed");
        roleService.saveRole(testRole);

        // then
        entityManager.clear();
        assertEquals(
                "changed",
                entityManager.find(RoleEntity.class, testRole.getRoleId()).getName()
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
                .name(testRole.getName())
                .build();
        Page<Role> rolePage = roleService.getRoles(roleSearch, PageRequest.of(0,10));

        // then
        assertTrue(rolePage.getContent().stream()
                .anyMatch(e -> e.getName().contains(roleSearch.getName())));
    }

}