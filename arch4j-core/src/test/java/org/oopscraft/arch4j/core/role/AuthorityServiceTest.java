package org.oopscraft.arch4j.core.role;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.oopscraft.arch4j.core.role.dao.AuthorityEntity;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class AuthorityServiceTest extends CoreTestSupport {

    final AuthorityService authorityService;

    Authority testAuthority = Authority.builder()
            .authorityId("role_id")
            .name("role_name")
            .build();

    @Test
    @Order(1)
    void saveAuthority() {
        authorityService.saveAuthority(testAuthority);
        assertNotNull(entityManager.find(AuthorityEntity.class, testAuthority.getAuthorityId()));
    }

    @Test
    @Order(2)
    void getAuthority() {
        Authority savedAuthority = authorityService.saveAuthority(testAuthority);
        Authority authority = authorityService.getAuthority(savedAuthority.getAuthorityId()).orElse(null);
        assertNotNull(authority);
    }

    @Test
    @Order(3)
    void deleteAuthority() {
        Authority savedAuthority = authorityService.saveAuthority(testAuthority);
        authorityService.deleteAuthority(savedAuthority.getAuthorityId());
        assertNull(entityManager.find(RoleEntity.class, savedAuthority.getAuthorityId()));
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