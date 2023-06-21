package org.oopscraft.arch4j.web.admin;

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

    /**
     * index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/message.html");
    }

    /**
     * get messages
     * @return variables
     */
    @GetMapping("get-messages")
    @ResponseBody
    public Page<Message> getVariables(MessageSearch messageSearch, Pageable pageable) {
        return messageService.getMessages(messageSearch, pageable);
    }

    /**
     * get message
     * @param messageId message id
     * @return message
     */
    @GetMapping("get-message")
    @ResponseBody
    public Message getMessage(@RequestParam("messageId")String messageId) {
        return messageService.getMessage(messageId)
                .orElseThrow();
    }

    /**
     * saves message
     * @param message message info
     * @return message
     */
    @PostMapping("save-message")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_MESSAGE_EDIT')")
    public Message saveMessage(@RequestBody @Valid Message message) {
        return messageService.saveMessage(message);
    }

    /**
     * deletes message
     * @param messageId message id
     */
    @GetMapping("delete-message")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_MESSAGE_EDIT')")
    public void deleteMessage(@RequestParam("messageId")String messageId) {
        messageService.deleteMessage(messageId);
    }

}
