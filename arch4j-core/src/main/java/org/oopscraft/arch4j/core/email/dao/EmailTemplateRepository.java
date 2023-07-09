package org.oopscraft.arch4j.core.email.dao;

import org.oopscraft.arch4j.core.email.EmailTemplateSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity,String>, JpaSpecificationExecutor<EmailTemplateEntity> {

    default Page<EmailTemplateEntity> findAll(EmailTemplateSearch emailTemplateSearch, Pageable pageable) {
        Specification<EmailTemplateEntity> specification = (root, query, criteriaBuilder) -> null;
        if(emailTemplateSearch.getTemplateId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(EmailTemplateEntity_.TEMPLATE_ID), '%' + emailTemplateSearch.getTemplateId() + '%'));
        }
        if(emailTemplateSearch.getTemplateName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(EmailTemplateEntity_.TEMPLATE_NAME), '%' + emailTemplateSearch.getTemplateName() + '%'));
        }
        return findAll(specification, pageable);
    }

}
