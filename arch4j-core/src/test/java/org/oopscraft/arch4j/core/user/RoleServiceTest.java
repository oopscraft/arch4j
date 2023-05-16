package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.RoleSearch;
import org.oopscraft.arch4j.core.role.RoleService;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.oopscraft.arch4j.core.role.repository.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class RoleServiceTest extends CoreTestSupport {

    final RoleService roleService;

    Role testRole = Role.builder()
            .id("role_id")
            .name("role_name")
            .build();

    @Test
    @Order(1)
    void saveRole() {
        roleService.saveRole(testRole);
        assertNotNull(entityManager.find(RoleEntity.class, testRole.getId()));
    }

    @Test
    @Order(2)
    void getRole() {

        // save test role
        saveRole();

        // get role
        Role role = roleService.getRole(testRole.getId()).orElse(null);

        // check
        assertNotNull(role);
    }

    @Test
    @Order(3)
    void deleteRole() {

        // save test data
        saveRole();

        // delete
        roleService.deleteRole(testRole.getId());

        // check
        assertNull(entityManager.find(RoleEntity.class, testRole.getId()));
    }

    @Test
    @Order(4)
    void getRoles() {

        // save test
        saveRole();

        // search by condition
        RoleSearch roleSearch = RoleSearch.builder()
                .name(testRole.getName())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Role> rolePage = roleService.getRoles(roleSearch, pageable);

        // check result
        assertTrue(rolePage.getContent().stream().anyMatch(e -> e.getName().contains(roleSearch.getName())));
    }

}