package org.oopscraft.arch4j.core.user.dao;

import org.oopscraft.arch4j.core.user.RoleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String>, JpaSpecificationExecutor<RoleEntity> {

    default Page<RoleEntity> findAll(RoleSearch roleSearch, Pageable pageable) {
        Specification<RoleEntity> specification = (root, query, criteriaBuilder) -> null;
        if(roleSearch.getRoleId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(RoleEntity_.ROLE_ID), '%' + roleSearch.getRoleId() + '%'));
        }
        if(roleSearch.getRoleName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(RoleEntity_.ROLE_NAME), '%' + roleSearch.getRoleName() + '%'));
        }
        return findAll(specification, pageable);
    }

}
