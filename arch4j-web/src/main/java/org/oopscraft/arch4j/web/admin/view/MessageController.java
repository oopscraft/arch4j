package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.message.Message;
import org.oopscraft.arch4j.core.message.MessageSearch;
import org.oopscraft.arch4j.core.message.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/message")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN_MESSAGE')")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ModelAndView message() {
        return new ModelAndView("admin/message.html");
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
    @PreAuthorize("hasAuthority('ADMIN_MESSAGE_EDIT')")
    public Message saveMessage(@RequestBody @Valid Message message) {
        return messageService.saveMessage(message);
    }

    @GetMapping("delete-message")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_MESSAGE_EDIT')")
    public void deleteMessage(@RequestParam("messageId")String messageId) {
        messageService.deleteMessage(messageId);
    }

}
