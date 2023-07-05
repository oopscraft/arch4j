package org.oopscraft.arch4j.core.email.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity,String>, JpaSpecificationExecutor<EmailTemplateEntity> {

}
