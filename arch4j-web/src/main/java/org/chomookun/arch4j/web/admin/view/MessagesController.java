package org.chomookun.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.message.model.Message;
import org.chomookun.arch4j.core.message.model.MessageSearch;
import org.chomookun.arch4j.core.message.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/messages")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('admin.messages')")
public class MessagesController {

    private final MessageService messageService;

    @GetMapping
    public ModelAndView messages() {
        return new ModelAndView("admin/messages.html");
    }

    @GetMapping("get-messages")
    @ResponseBody
    public Page<Message> getMessages(MessageSearch messageSearch, Pageable pageable) {
        return messageService.getMessages(messageSearch, pageable);
    }

    @GetMapping("get-message")
    @ResponseBody
    public Message getMessage(@RequestParam("messageId")String messageId) {
        return messageService.getMessage(messageId)
                .orElseThrow();
    }

    @PostMapping("save-message")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.messages.edit')")
    public Message saveMessage(@RequestBody @Valid Message message) {
        return messageService.saveMessage(message);
    }

    @GetMapping("delete-message")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.messages.edit')")
    public void deleteMessage(@RequestParam("messageId")String messageId) {
        messageService.deleteMessage(messageId);
    }

}
