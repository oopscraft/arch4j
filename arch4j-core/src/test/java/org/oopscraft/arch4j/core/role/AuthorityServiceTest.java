package org.oopscraft.arch4j.core.role;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.role.Authority;
import org.oopscraft.arch4j.core.role.AuthoritySearch;
import org.oopscraft.arch4j.core.role.AuthorityService;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.oopscraft.arch4j.core.role.repository.AuthorityEntity;
import org.oopscraft.arch4j.core.role.repository.RoleEntity;
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
        Authority savedAuthority = authorityService.saveAuthority(testAuthority);
        Authority authority = authorityService.getAuthority(savedAuthority.getId()).orElse(null);
        assertNotNull(authority);
    }

    @Test
    @Order(3)
    void deleteAuthority() {
        Authority savedAuthority = authorityService.saveAuthority(testAuthority);
        authorityService.deleteAuthority(savedAuthority.getId());
        assertNull(entityManager.find(RoleEntity.class, savedAuthority.getId()));
    }

    @Test
    @Order(4)
    void getAuthorities() {
        Authority savedAuthority = authorityService.saveAuthority(testAuthority);
        AuthoritySearch authoritySearch = AuthoritySearch.builder()
                .name(savedAuthority.getName())
                .build();
        Page<Authority> rolePage = authorityService.getAuthorities(authoritySearch, PageRequest.of(0,10));
        assertTrue(rolePage.getContent().stream().anyMatch(e -> e.getName().contains(authoritySearch.getName())));
    }

}