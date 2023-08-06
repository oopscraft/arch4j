package org.oopscraft.arch4j.core.user.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.oopscraft.arch4j.core.user.dao.AuthorityEntity;
import org.oopscraft.arch4j.core.user.dao.AuthorityRepository;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class AuthorityRepositoryTest extends CoreTestSupport {

    private final AuthorityRepository authorityRepository;

    private AuthorityEntity getTestAuthorityEntity() {
        return AuthorityEntity.builder()
                .authorityId("test-auth")
                .authorityName("test auth")
                .build();
    }

    private AuthorityEntity saveTestAuthorityEntity() {
        AuthorityEntity authorityEntity = getTestAuthorityEntity();
        entityManager.persist(authorityEntity);
        entityManager.flush();
        entityManager.clear();
        return authorityEntity;
    }

    @Test
    @Order(1)
    void saveToPersist() {
        // given
        AuthorityEntity testAuthorityEntity = getTestAuthorityEntity();

        // when
        AuthorityEntity savedAuthorityEntity = authorityRepository.saveAndFlush(testAuthorityEntity);

        // then
        entityManager.flush();
        entityManager.clear();
        assertNotNull(entityManager.find(AuthorityEntity.class, savedAuthorityEntity.getAuthorityId()));
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        AuthorityEntity testAuthorityEntity = saveTestAuthorityEntity();

        // when
        AuthorityEntity authorityEntity = entityManager.find(AuthorityEntity.class, testAuthorityEntity.getAuthorityId());
        authorityEntity.setAuthorityName("changed role name");
        authorityRepository.saveAndFlush(authorityEntity);

        // then
        entityManager.clear();
        assertEquals(
                "changed role name",
                entityManager.find(AuthorityEntity.class, testAuthorityEntity.getAuthorityId()).getAuthorityName()
        );
    }

    @Test
    @Order(2)
    void findById() {
        // given
        AuthorityEntity testAuthorityEntity = saveTestAuthorityEntity();

        // when
        AuthorityEntity authorityEntity = authorityRepository.findById(testAuthorityEntity.getAuthorityId()).orElseThrow();

        // then
        assertEquals(testAuthorityEntity.getAuthorityId(), authorityEntity.getAuthorityId());
    }

    @Test
    @Order(3)
    void deleteById() {
        // given
        AuthorityEntity testAuthorityEntity = saveTestAuthorityEntity();

        // when
        authorityRepository.deleteById(testAuthorityEntity.getAuthorityId());

        // then
        entityManager.flush();
        entityManager.clear();
        assertNull(entityManager.find(AuthorityEntity.class, testAuthorityEntity.getAuthorityId()));
    }
}