package org.oopscraft.arch4j.core.email.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.support.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class EmailTemplateRepositoryTest extends CoreTestSupport {

    private final EmailTemplateRepository emailTemplateRepository;

    private EmailTemplateEntity getTestEmailTemplateEntity() {
        return EmailTemplateEntity.builder()
                .templateId(IdGenerator.uuid())
                .templateName("junit test template")
                .build();
    }

    private EmailTemplateEntity createTestEmailTemplateEntity() {
        EmailTemplateEntity testEmailTemplateEntity = getTestEmailTemplateEntity();
        entityManager.persist(testEmailTemplateEntity);
        entityManager.flush();
        entityManager.clear();
        return testEmailTemplateEntity;
    }

    @Test
    @Order(1)
    void save() {
        // given
        EmailTemplateEntity testEmailTemplateEntity = getTestEmailTemplateEntity();

        // when
        emailTemplateRepository.saveAndFlush(testEmailTemplateEntity);
        testEmailTemplateEntity.setTemplateName("changed template name");
        emailTemplateRepository.saveAndFlush(testEmailTemplateEntity);

        // then
        EmailTemplateEntity savedEmailTemplateEntity = entityManager.find(EmailTemplateEntity.class, testEmailTemplateEntity.getTemplateId());
        assertNotNull(savedEmailTemplateEntity);
        assertEquals(testEmailTemplateEntity.getTemplateName(), savedEmailTemplateEntity.getTemplateName());
    }

    @Test
    @Order(2)
    void findById() {
        // given
        EmailTemplateEntity testEmailTemplateEntity = createTestEmailTemplateEntity();

        // when
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findById(testEmailTemplateEntity.getTemplateId()).orElseThrow();

        // then
        assertEquals(testEmailTemplateEntity.getTemplateId(), emailTemplateEntity.getTemplateId());
    }

    @Test
    @Order(3)
    void deleteById() {
        // given
        EmailTemplateEntity testEmailTemplateEntity = createTestEmailTemplateEntity();

        // when
        emailTemplateRepository.deleteById(testEmailTemplateEntity.getTemplateId());

        // then
        assertNull(entityManager.find(EmailTemplateEntity.class, testEmailTemplateEntity.getTemplateId()));
    }

}