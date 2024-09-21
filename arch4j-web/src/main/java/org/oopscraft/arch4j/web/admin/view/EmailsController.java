package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.email.service.EmailService;
import org.oopscraft.arch4j.core.email.model.Email;
import org.oopscraft.arch4j.core.email.model.EmailSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/emails")
@PreAuthorize("hasAuthority('admin.emails')")
@RequiredArgsConstructor
public class EmailsController {

    private final EmailService emailService;

    @GetMapping
    public ModelAndView emails() {
        return new ModelAndView("admin/emails.html");
    }

    @GetMapping("get-emails")
    @ResponseBody
    public Page<Email> getEmailTemplates(EmailSearch emailSearch, Pageable pageable) {
        return emailService.getEmails(emailSearch, pageable);
    }

    @GetMapping("get-email")
    @ResponseBody
    public Email getEmail(@RequestParam("emailId")String emailId) {
        return emailService.getEmail(emailId)
                .orElseThrow();
    }

    @PostMapping("save-email")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.emails.edit')")
    public Email saveEmail(@RequestBody @Valid Email email) {
        return emailService.saveEmail(email);
    }

    @GetMapping("delete-email")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.emails.edit')")
    public void deleteEmail(@RequestParam("emailId")String emailId) {
        emailService.deleteEmail(emailId);
    }

}
