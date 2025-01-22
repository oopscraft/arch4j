package org.chomookun.arch4j.core.email.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class EmailTemplateRepositoryTest extends CoreTestSupport {

    private final EmailRepository emailTemplateRepository;

    private EmailEntity getTestEmailTemplateEntity() {
        return EmailEntity.builder()
                .emailId(IdGenerator.uuid())
                .name("junit test template")
                .build();
    }

    private EmailEntity createTestEmailTemplateEntity() {
        EmailEntity testEmailTemplateEntity = getTestEmailTemplateEntity();
        entityManager.persist(testEmailTemplateEntity);
        entityManager.flush();
        entityManager.clear();
        return testEmailTemplateEntity;
    }

    @Test
    @Order(1)
    void save() {
        // given
        EmailEntity testEmailTemplateEntity = getTestEmailTemplateEntity();

        // when
        emailTemplateRepository.saveAndFlush(testEmailTemplateEntity);
        testEmailTemplateEntity.setName("changed template name");
        emailTemplateRepository.saveAndFlush(testEmailTemplateEntity);

        // then
        EmailEntity savedEmailTemplateEntity = entityManager.find(EmailEntity.class, testEmailTemplateEntity.getEmailId());
        assertNotNull(savedEmailTemplateEntity);
        assertEquals(testEmailTemplateEntity.getName(), savedEmailTemplateEntity.getName());
    }

    @Test
    @Order(2)
    void findById() {
        // given
        EmailEntity testEmailTemplateEntity = createTestEmailTemplateEntity();

        // when
        EmailEntity emailTemplateEntity = emailTemplateRepository.findById(testEmailTemplateEntity.getEmailId()).orElseThrow();

        // then
        assertEquals(testEmailTemplateEntity.getEmailId(), emailTemplateEntity.getEmailId());
    }

    @Test
    @Order(3)
    void deleteById() {
        // given
        EmailEntity testEmailTemplateEntity = createTestEmailTemplateEntity();

        // when
        emailTemplateRepository.deleteById(testEmailTemplateEntity.getEmailId());

        // then
        assertNull(entityManager.find(EmailEntity.class, testEmailTemplateEntity.getEmailId()));
    }

}