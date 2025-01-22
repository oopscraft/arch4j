package org.chomoo.arch4j.core.email.dao;

import org.chomoo.arch4j.core.email.model.EmailSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity,String>, JpaSpecificationExecutor<EmailEntity> {

    default Page<EmailEntity> findAll(EmailSearch emailSearch, Pageable pageable) {
        Specification<EmailEntity> specification = (root, query, criteriaBuilder) -> null;
        if(emailSearch.getEmailId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(EmailEntity_.EMAIL_ID), '%' + emailSearch.getEmailId() + '%'));
        }
        if(emailSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(EmailEntity_.NAME), '%' + emailSearch.getName() + '%'));
        }
        return findAll(specification, pageable);
    }

}
