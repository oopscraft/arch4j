package org.oopscraft.arch4j.core.email;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.email.dao.EmailTemplateEntity;
import org.oopscraft.arch4j.core.email.dao.EmailTemplateRepository;
import org.oopscraft.arch4j.core.email.dao.EmailTemplateSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailTemplateRepository emailTemplateRepository;

    private final JavaMailSender javaMailSender;

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

        emailTemplateEntity.setTemplateId(emailTemplate.getTemplateName());
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
     * process template
     * @param template template content
     * @param variables bind variables
     * @return processed content
     */
    private String processTemplate(String template, Map<String,Object> variables) {
        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
        Context context = new Context();
        variables.forEach(context::setVariable);
        return templateEngine.process(template, context);
    }

    /**
     * send email with email template
     * @param to to email address
     * @param emailTemplate email template object
     * @throws EmailException email exception
     */
    public void sendEmailWidthTemplate(String to, EmailTemplate emailTemplate) throws EmailException {
        String subject = processTemplate(emailTemplate.getSubject(), emailTemplate.getVariables());
        String content = processTemplate(emailTemplate.getContent(), emailTemplate.getVariables());
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

}
