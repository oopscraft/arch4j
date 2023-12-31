package org.oopscraft.arch4j.core.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.email.dao.EmailTemplateEntity;
import org.oopscraft.arch4j.core.support.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
@Slf4j
class EmailServiceTest extends CoreTestSupport {

    private final EmailService emailService;

    EmailTemplateEntity testEmailTemplateEntity = EmailTemplateEntity.builder()
            .templateId("test_email")
            .templateName("test_name")
            .subject("subject ${subject}")
            .content("content ${content}")
            .build();

    @Test
    public void saveEmailTemplate() {
        EmailTemplate emailTemplate = EmailTemplate.from(testEmailTemplateEntity);
        EmailTemplate savedEmailTemplate = emailService.saveEmailTemplate(emailTemplate);
        assertNotNull(entityManager.find(EmailTemplateEntity.class, savedEmailTemplate.getTemplateId()));
    }

    @Test
    public void getEmailTemplate() {
        entityManager.persist(testEmailTemplateEntity);
        EmailTemplate emailTemplate = emailService.getEmailTemplate(testEmailTemplateEntity.getTemplateId())
                .orElseThrow();
        assertNotNull(emailTemplate);
    }

    @Test
    public void sendEmail() {
        try {
            emailService.sendEmail("oopscraft.org@gmail.com","test email subject","test email content");
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void sendEmailWithTemplate() {
        entityManager.persist(testEmailTemplateEntity);

        EmailTemplate emailTemplate = emailService.getEmailTemplate(testEmailTemplateEntity.getTemplateId()).orElseThrow();
        emailTemplate.addVariable("subject", "test subject");
        emailTemplate.addVariable("content", "<b>test content</b> test content");

        try {
            emailService.sendEmailWidthTemplate("oopscraft.org@gmail.com", emailTemplate);
        }catch(EmailException e) {
            throw new RuntimeException(e);
        }
    }

}