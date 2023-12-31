package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.email.EmailService;
import org.oopscraft.arch4j.core.email.EmailTemplate;
import org.oopscraft.arch4j.core.email.EmailTemplateSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/email")
@PreAuthorize("hasAuthority('ADMIN_EMAIL')")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping
    public ModelAndView email() {
        return new ModelAndView("admin/email.html");
    }

    @GetMapping("get-email-templates")
    @ResponseBody
    public Page<EmailTemplate> getEmailTemplates(EmailTemplateSearch emailTemplateSearch, Pageable pageable) {
        return emailService.getEmailTemplates(emailTemplateSearch, pageable);
    }

    @GetMapping("get-email-template")
    @ResponseBody
    public EmailTemplate getEmailTemplate(@RequestParam("templateId")String templateId) {
        return emailService.getEmailTemplate(templateId)
                .orElseThrow();
    }

    @PostMapping("save-email-template")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN_EMAIL_EDIT')")
    public EmailTemplate saveEmailTemplate(@RequestBody @Valid EmailTemplate emailTemplate) {
        return emailService.saveEmailTemplate(emailTemplate);
    }

    @GetMapping("delete-email-template")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN_EMAIL_EDIT')")
    public void deleteEmailTemplate(@RequestParam("templateId")String templateId) {
        emailService.deleteEmailTemplate(templateId);
    }

}
