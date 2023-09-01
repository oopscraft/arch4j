package org.oopscraft.arch4j.core.role.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.oopscraft.arch4j.core.role.dao.AuthorityEntity;
import org.oopscraft.arch4j.core.role.dao.RoleAuthorityEntity;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.role.dao.RoleRepository;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class RoleRepositoryTest extends CoreTestSupport {

    private final RoleRepository roleRepository;

    private RoleEntity getTestRoleEntity() {

        RoleEntity roleEntity = RoleEntity.builder()
                .roleId("test-role")
                .roleName("test role")
                .build();

        // add authority
        Arrays.asList("auth-1","auto-2").forEach(authorityId -> {
            entityManager.persist(AuthorityEntity.builder()
                    .authorityId(authorityId)
                    .authorityName("name of " + authorityId)
                    .build());
            entityManager.flush();

            RoleAuthorityEntity roleAuthorityEntity = RoleAuthorityEntity.builder()
                    .roleId(roleEntity.getRoleId())
                    .authorityId(authorityId)
                    .build();
            roleEntity.getRoleAuthorityEntities()
                    .add(roleAuthorityEntity);
        });
        return roleEntity;
    }

    private RoleEntity saveTestRoleEntity() {
        RoleEntity roleEntity = getTestRoleEntity();
        entityManager.persist(roleEntity);
        entityManager.flush();
        entityManager.clear();
        return roleEntity;
    }

    @Test
    @Order(1)
    void saveToPersist() {
        // given
        RoleEntity testRoleEntity = getTestRoleEntity();

        // when
        RoleEntity savedRoleEntity = roleRepository.saveAndFlush(testRoleEntity);

        // then
        entityManager.clear();
        assertNotNull(entityManager.find(RoleEntity.class, savedRoleEntity.getRoleId()));
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        RoleEntity testRoleEntity = saveTestRoleEntity();

        // when
        RoleEntity roleEntity = entityManager.find(RoleEntity.class, testRoleEntity.getRoleId());
        roleEntity.setRoleName("changed role name");
        roleRepository.saveAndFlush(roleEntity);

        // then
        entityManager.clear();
        assertEquals(
                "changed role name",
                entityManager.find(RoleEntity.class, testRoleEntity.getRoleId()).getRoleName()
        );
    }

    @Test
    @Order(2)
    void findById() {
        // given
        RoleEntity testRoleEntity = saveTestRoleEntity();

        // when
        RoleEntity roleEntity = roleRepository.findById(testRoleEntity.getRoleId()).orElseThrow();

        // then
        assertEquals(testRoleEntity.getRoleId(), roleEntity.getRoleId());
    }

    @Test
    @Order(3)
    void deleteById() {
        // given
        RoleEntity testRoleEntity = saveTestRoleEntity();

        // when
        roleRepository.deleteById(testRoleEntity.getRoleId());

        // then
        entityManager.flush();
        entityManager.clear();
        assertNull(entityManager.find(RoleEntity.class, testRoleEntity.getRoleId()));
    }
}