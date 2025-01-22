package org.chomookun.arch4j.core.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.email.dao.EmailEntity;
import org.chomookun.arch4j.core.email.exception.EmailException;
import org.chomookun.arch4j.core.email.model.Email;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
@Slf4j
class EmailServiceTest extends CoreTestSupport {

    private final EmailService emailService;

    EmailEntity testEmailTemplateEntity = EmailEntity.builder()
            .emailId("test_email")
            .name("test_name")
            .subject("subject ${subject}")
            .content("content ${content}")
            .build();

    @Test
    public void saveEmailTemplate() {
        Email emailTemplate = Email.from(testEmailTemplateEntity);
        Email savedEmailTemplate = emailService.saveEmail(emailTemplate);
        assertNotNull(entityManager.find(EmailEntity.class, savedEmailTemplate.getEmailId()));
    }

    @Test
    public void getEmailTemplate() {
        entityManager.persist(testEmailTemplateEntity);
        Email emailTemplate = emailService.getEmail(testEmailTemplateEntity.getEmailId())
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

        Email emailTemplate = emailService.getEmail(testEmailTemplateEntity.getEmailId()).orElseThrow();
        emailTemplate.addVariable("subject", "test subject");
        emailTemplate.addVariable("content", "<b>test content</b> test content");

        try {
            emailService.sendEmailWidthTemplate("oopscraft.org@gmail.com", emailTemplate);
        }catch(EmailException e) {
            throw new RuntimeException(e);
        }
    }

}