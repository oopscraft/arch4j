package org.oopscraft.arch4j.core.email;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.email.dao.*;
import org.oopscraft.arch4j.core.message.MessageSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService implements InitializingBean {

    private final EmailTemplateRepository emailTemplateRepository;

    private final EmailVerificationRepository emailVerificationRepository;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private SpringTemplateEngine templateEngine;

    @Override
    public void afterPropertiesSet() throws Exception {
        templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateEngineMessageSource(messageSource);
        templateEngine.setMessageSource(messageSource);
        templateEngine.setEnableSpringELCompiler(true);
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
    }

    /**
     * save email template
     * @param emailTemplate email template
     * @return saved email template
     */
    public EmailTemplate saveEmailTemplate(EmailTemplate emailTemplate) {
        EmailTemplateEntity emailTemplateEntity = Optional.ofNullable(emailTemplate.getTemplateId())
                .flatMap(emailTemplateRepository::findById)
                .orElse(null);

        if(emailTemplateEntity == null) {
            emailTemplateEntity = EmailTemplateEntity.builder()
                    .templateId(emailTemplate.getTemplateId())
                    .build();
        }

        emailTemplateEntity.setTemplateName(emailTemplate.getTemplateName());
        emailTemplateEntity.setSubject(emailTemplate.getSubject());
        emailTemplateEntity.setContent(emailTemplate.getContent());

        emailTemplateEntity = emailTemplateRepository.saveAndFlush(emailTemplateEntity);
        return EmailTemplate.from(emailTemplateEntity);
    }

    /**
     * search email template
     * @param emailTemplateSearch search condition
     * @param pageable pagination info
     * @return list of email template
     */
    public Page<EmailTemplate> getEmailTemplates(EmailTemplateSearch emailTemplateSearch, Pageable pageable) {

        Specification<EmailTemplateEntity> specification = (root, query, criteriaBuilder) -> null;
        if(emailTemplateSearch.getTemplateId() != null) {
            specification = specification.and(EmailTemplateSpecification.likeTemplateId(emailTemplateSearch.getTemplateId()));
        }
        if(emailTemplateSearch.getTemplateName() != null) {
            specification = specification.and(EmailTemplateSpecification.likeTemplateName(emailTemplateSearch.getTemplateName()));
        }

        Page<EmailTemplateEntity> emailTemplatePage = emailTemplateRepository.findAll(specification, pageable);
        List<EmailTemplate> emailTemplates = emailTemplatePage.getContent().stream()
                        .map(EmailTemplate::from)
                        .collect(Collectors.toList());
        long total = emailTemplatePage.getTotalElements();

        return new PageImpl<>(emailTemplates, pageable, total);
    }

    /**
     * delete email template
     * @param templateId template id
     * @return email template
     */
    public Optional<EmailTemplate> getEmailTemplate(String templateId) {
        return emailTemplateRepository.findById(templateId)
                .map(EmailTemplate::from);
    }

    /**
     * delete email template
     * @param templateId template id
     */
    public void deleteEmailTemplate(String templateId) {
        emailTemplateRepository.deleteById(templateId);
        emailTemplateRepository.flush();
    }

    /**
     * send email with email template
     * @param to to email address
     * @param emailTemplate email template object
     * @throws EmailException email exception
     */
    public void sendEmailWidthTemplate(String to, EmailTemplate emailTemplate) throws EmailException {
        Context context = new Context();
        emailTemplate.getVariables().forEach(context::setVariable);
        String subject = templateEngine.process(emailTemplate.getSubject(), context);
        String content = templateEngine.process(emailTemplate.getContent(), context);
        sendEmail(to, subject, content);
    }

    /**
     * send email plain text
     * @param to to email address
     * @param subject subject
     * @param content content
     * @throws EmailException email exception
     */
    public void sendEmail(String to, String subject, String content) throws EmailException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content);
        } catch (MessagingException e) {
            throw new EmailException(e.getMessage(), e);
        }
        javaMailSender.send(mimeMessage);
    }

    /**
     * issue email verification
     * @param email email
     */
    public void issueEmailVerification(String email) {

        // answer
        SecureRandom random = new SecureRandom();
        String answer = String.valueOf(100000 + random.nextInt(89999));

        // send email
        EmailTemplate emailTemplate = getEmailTemplate("VERIFICATION").orElseThrow();
        emailTemplate.addVariable("answer", answer);
        try {
            sendEmailWidthTemplate(email, emailTemplate);
        } catch (Throwable t) {
            try {
                sendEmail(email, "Email Verification Answer", answer);
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }
        }

        // save entity
        EmailVerificationEntity emailVerificationEntity = EmailVerificationEntity.builder()
                .email(email)
                .issuedAt(LocalDateTime.now())
                .answer(answer)
                .build();
        emailVerificationRepository.saveAndFlush(emailVerificationEntity);
    }

    /**
     * check email verification
     * @param email email
     * @param answer answer
     */
    public void checkEmailVerification(String email, String answer) {
        EmailVerificationEntity emailVerificationEntity = emailVerificationRepository.findById(email).orElseThrow();
        if(answer.contentEquals(emailVerificationEntity.getAnswer())) {
            return;
        }
        throw new RuntimeException("answer is correct");
    }

}
