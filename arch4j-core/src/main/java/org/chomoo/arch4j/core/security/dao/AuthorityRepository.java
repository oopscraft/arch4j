package org.chomoo.arch4j.core.security.dao;

import org.chomoo.arch4j.core.security.model.AuthoritySearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        if(authoritySearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(AuthorityEntity_.NAME), '%' + authoritySearch.getName() + '%'));
        }

        pageable = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSort().and(Sort.by(AuthorityEntity_.SYSTEM_REQUIRED).descending())
        );

        return findAll(specification, pageable);
    }

}
