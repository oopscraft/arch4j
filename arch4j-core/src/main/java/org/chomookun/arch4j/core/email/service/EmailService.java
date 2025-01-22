package org.chomookun.arch4j.core.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.email.dao.EmailEntity;
import org.chomookun.arch4j.core.email.dao.EmailRepository;
import org.chomookun.arch4j.core.email.dao.EmailVerificationEntity;
import org.chomookun.arch4j.core.email.dao.EmailVerificationRepository;
import org.chomookun.arch4j.core.email.exception.EmailException;
import org.chomookun.arch4j.core.email.model.Email;
import org.chomookun.arch4j.core.email.model.EmailSearch;
import org.chomookun.arch4j.core.message.service.MessageSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements InitializingBean {

    private final EmailRepository emailRepository;

    private final EmailVerificationRepository emailVerificationRepository;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private SpringTemplateEngine templateEngine;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

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

    @Transactional
    public Email saveEmail(Email email) {
        EmailEntity emailEntity = Optional.ofNullable(email.getEmailId())
                .flatMap(emailRepository::findById)
                .orElse(EmailEntity.builder()
                    .emailId(email.getEmailId())
                    .build());

        emailEntity.setSystemUpdatedAt(LocalDateTime.now());    // disable dirty checking
        emailEntity.setName(email.getName());
        emailEntity.setSubject(email.getSubject());
        emailEntity.setContent(email.getContent());

        EmailEntity savedEmailEntity = emailRepository.saveAndFlush(emailEntity);
        return Email.from(savedEmailEntity);
    }

    public Page<Email> getEmails(EmailSearch emailSearch, Pageable pageable) {
        Page<EmailEntity> emailPage = emailRepository.findAll(emailSearch, pageable);
        List<Email> emails = emailPage.getContent().stream()
                        .map(Email::from)
                        .collect(Collectors.toList());
        return new PageImpl<>(emails, pageable, emailPage.getTotalElements());
    }

    public Optional<Email> getEmail(String emailId) {
        return emailRepository.findById(emailId)
                .map(Email::from);
    }

    @Transactional
    public void deleteEmail(String emailId) {
        emailRepository.deleteById(emailId);
        emailRepository.flush();
    }

    @Transactional
    public void sendEmailWidthTemplate(String to, Email emailTemplate) throws EmailException {
        Context context = new Context();
        emailTemplate.getVariables().forEach(context::setVariable);
        String subject = templateEngine.process(emailTemplate.getSubject(), context);
        String content = templateEngine.process(emailTemplate.getContent(), context);
        sendEmail(to, subject, content);
    }

    @Transactional
    public void sendEmail(String to, String subject, String content) throws EmailException {
        executorService.submit(() -> {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
                mimeMessageHelper.setTo(to);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(content);
                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }

    @Transactional
    public void issueEmailVerification(String email) {

        // answer
        SecureRandom random = new SecureRandom();
        String answer = String.valueOf(100000 + random.nextInt(89999));

        // send email
        Email emailTemplate = getEmail("VERIFICATION").orElseThrow();
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

    public void validateEmailVerification(String email, String answer) {
        EmailVerificationEntity emailVerificationEntity = emailVerificationRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("email verification request is not found."));
        if(!answer.contentEquals(emailVerificationEntity.getAnswer())) {
            throw new RuntimeException("answer is incorrect");
        }
        if(emailVerificationEntity.getIssuedAt().isBefore(LocalDateTime.now().minusMinutes(10))) {
            throw new RuntimeException("verification is expired");
        }
    }

}
