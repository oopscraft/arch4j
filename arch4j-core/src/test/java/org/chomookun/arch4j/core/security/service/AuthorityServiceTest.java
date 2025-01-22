package org.chomookun.arch4j.core.security.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.security.dao.AuthorityEntity;
import org.chomookun.arch4j.core.security.model.Authority;
import org.chomookun.arch4j.core.security.model.AuthoritySearch;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class AuthorityServiceTest extends CoreTestSupport {

    private final AuthorityService authorityService;

    private Authority getTestAuthority() {
        return Authority.builder()
                .authorityId("role_id")
                .name("role_name")
                .build();
    }

    private Authority saveTestAuthority() {
        Authority testAuthority = getTestAuthority();
        return authorityService.saveAuthority(testAuthority);
    }

    @Test
    @Order(1)
    void saveAuthorityToPersist() {
        // given
        Authority testAuthority = getTestAuthority();

        // when
        authorityService.saveAuthority(testAuthority);

        // then
        entityManager.clear();
        AuthorityEntity authorityEntity = entityManager.find(AuthorityEntity.class, testAuthority.getAuthorityId());
        assertNotNull(authorityEntity);
    }

    @Test
    @Order(2)
    void saveAuthorityToMerge() {
        // given
        Authority testAuthority = saveTestAuthority();

        // when
        testAuthority.setName("changed");
        authorityService.saveAuthority(testAuthority);

        // then
        entityManager.clear();
        assertEquals(
                "changed",
                entityManager.find(AuthorityEntity.class, testAuthority.getAuthorityId()).getName()
        );
    }

    @Test
    @Order(3)
    void getAuthority() {
        // given
        Authority testAuthority = saveTestAuthority();

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
    @Order(4)
    void deleteAuthority() {
        // given
        Authority testAuthority = saveTestAuthority();

        // when
        authorityService.deleteAuthority(testAuthority.getAuthorityId());

        // then
        entityManager.flush();
        entityManager.clear();
        AuthorityEntity authorityEntity = entityManager.find(AuthorityEntity.class, testAuthority.getAuthorityId());
        assertNull(authorityEntity);
    }

    @Test
    @Order(5)
    void getAuthorities() {
        // given
        Authority testAuthority = saveTestAuthority();

        // when
        AuthoritySearch authoritySearch = AuthoritySearch.builder()
                .authorityId(testAuthority.getAuthorityId())
                .build();
        Page<Authority> rolePage = authorityService.getAuthorities(authoritySearch, PageRequest.of(0,10));

        // then
        assertTrue(rolePage.getContent().stream()
                .allMatch(e ->
                        e.getAuthorityId().contains(authoritySearch.getAuthorityId())
                )
        );
    }

}