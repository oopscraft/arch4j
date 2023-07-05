package org.oopscraft.arch4j.core.email.dao;

import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity_;
import org.springframework.data.jpa.domain.Specification;

public class EmailTemplateSpecification {

    public static Specification<EmailTemplateEntity> likeTemplateId(String templateId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(EmailTemplateEntity_.TEMPLATE_ID), '%' + templateId + '%');
    }

    public static Specification<EmailTemplateEntity> likeTemplateName(String templateName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(EmailTemplateEntity_.TEMPLATE_NAME), '%' + templateName + '%');
    }

}
