package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.oopscraft.arch4j.core.user.repository.AuthorityEntity;
import org.oopscraft.arch4j.core.user.repository.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class AuthorityServiceTest extends CoreTestSupport {

    final AuthorityService authorityService;

    Authority testAuthority = Authority.builder()
            .id("role_id")
            .name("role_name")
            .build();

    @Test
    @Order(1)
    void saveAuthority() {
        authorityService.saveAuthority(testAuthority);
        assertNotNull(entityManager.find(AuthorityEntity.class, testAuthority.getId()));
    }

    @Test
    @Order(2)
    void getAuthority() {

        // save test role
        saveAuthority();

        // get role
        Authority authority = authorityService.getAuthority(testAuthority.getId()).orElse(null);

        // check
        assertNotNull(authority);
    }

    @Test
    @Order(3)
    void deleteRole() {

        // save test data
        saveAuthority();

        // delete
        authorityService.deleteAuthority(testAuthority.getId());

        // check
        assertNull(entityManager.find(RoleEntity.class, testAuthority.getId()));
    }

    @Test
    @Order(4)
    void getRoles() {

        // save test
        saveAuthority();

        // search by condition
        AuthoritySearch authoritySearch = AuthoritySearch.builder()
                .name(testAuthority.getName())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Authority> rolePage = authorityService.getAuthorities(authoritySearch, pageable);

        // check result
        assertTrue(rolePage.getContent().stream().anyMatch(e -> e.getName().contains(authoritySearch.getName())));
    }

}