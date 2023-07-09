package org.oopscraft.arch4j.core.role.dao;

import org.oopscraft.arch4j.core.role.AuthoritySearch;
import org.oopscraft.arch4j.core.user.dao.UserRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, String>, JpaSpecificationExecutor<AuthorityEntity>, UserRepositoryCustom {

    default Page<AuthorityEntity> findAll(AuthoritySearch authoritySearch, Pageable pageable) {
        Specification<AuthorityEntity> specification = (root, query, criteriaBuilder) -> null;
        if(authoritySearch.getAuthorityId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(AuthorityEntity_.AUTHORITY_ID), '%' + authoritySearch.getAuthorityId() + '%'));
        }
        if(authoritySearch.getAuthorityName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(AuthorityEntity_.AUTHORITY_NAME), '%' + authoritySearch.getAuthorityName() + '%'));
        }
        return findAll(specification, pageable);
    }

}
