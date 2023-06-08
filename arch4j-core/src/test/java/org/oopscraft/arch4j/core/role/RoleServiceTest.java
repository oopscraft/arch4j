package org.oopscraft.arch4j.core.role;

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
        Role savedRole = roleService.saveRole(testRole);
        assertNotNull(savedRole);
        assertNotNull(entityManager.find(RoleEntity.class, savedRole.getId()));
    }

    @Test
    @Order(2)
    void getRole() {
        Role savedRole = roleService.saveRole(testRole);
        Role role = roleService.getRole(savedRole.getId()).orElse(null);
        assertNotNull(role);
    }

    @Test
    @Order(3)
    void deleteRole() {
        Role savedRole = roleService.saveRole(testRole);
        roleService.deleteRole(savedRole.getId());
        assertNull(entityManager.find(RoleEntity.class, testRole.getId()));
    }

    @Test
    @Order(4)
    void getRoles() {
        Role savedRole = roleService.saveRole(testRole);
        RoleSearch roleSearch = RoleSearch.builder()
                .name(savedRole.getName())
                .build();
        Page<Role> rolePage = roleService.getRoles(roleSearch, PageRequest.of(0,10));
        assertTrue(rolePage.getContent().stream().anyMatch(e -> e.getName().contains(roleSearch.getName())));
    }

}