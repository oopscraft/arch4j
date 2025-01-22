package org.chomookun.arch4j.core.security.dao;

import org.chomookun.arch4j.core.security.model.RoleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        if(roleSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(RoleEntity_.NAME), '%' + roleSearch.getName() + '%'));
        }

        pageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().and(Sort.by(RoleEntity_.SYSTEM_REQUIRED).descending())
        );

        return findAll(specification, pageable);
    }

}
