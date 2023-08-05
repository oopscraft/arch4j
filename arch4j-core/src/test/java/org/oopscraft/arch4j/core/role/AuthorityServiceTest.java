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

    private final AuthorityService authorityService;

    private Authority getTestAuthority() {
        return Authority.builder()
                .authorityId("role_id")
                .authorityName("role_name")
                .build();
    }

    private Authority createTestAuthority() {
        Authority testAuthority = getTestAuthority();
        return authorityService.saveAuthority(testAuthority);
    }

    @Test
    @Order(1)
    void saveAuthority() {
        // given
        Authority testAuthority = getTestAuthority();

        // when
        authorityService.saveAuthority(testAuthority);

        // then
        AuthorityEntity authorityEntity = entityManager.find(AuthorityEntity.class, testAuthority.getAuthorityId());
        assertNotNull(authorityEntity);
    }

    @Test
    @Order(2)
    void getAuthority() {
        // given
        Authority testAuthority = createTestAuthority();

        // when
        Authority authority = authorityService.getAuthority(testAuthority.getAuthorityId())
                .orElseThrow();

        // then
        assertEquals(
                testAuthority.getAuthorityId(),
                authority.getAuthorityId()
        );
    }

    @Test
    @Order(3)
    void getAuthorities() {
        // given
        Authority testAuthority = createTestAuthority();

        // when
        AuthoritySearch authoritySearch = AuthoritySearch.builder()
                .authorityId(testAuthority.getAuthorityId())
                .build();
        Page<Authority> rolePage = authorityService.getAuthorities(authoritySearch, PageRequest.of(0,10));

        // then
        assertTrue(rolePage.getContent().stream()
                .anyMatch(e ->
                        e.getAuthorityId().contains(authoritySearch.getAuthorityId())
                )
        );
    }



    @Test
    @Order(4)
    void deleteAuthority() {
        // given
        Authority testAuthority = createTestAuthority();

        // when
        authorityService.deleteAuthority(testAuthority.getAuthorityId());

        // then
        AuthorityEntity authorityEntity = entityManager.find(AuthorityEntity.class, testAuthority.getAuthorityId());
        assertNull(authorityEntity);
    }


}