package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.message.Message;
import org.oopscraft.arch4j.core.message.MessageSearch;
import org.oopscraft.arch4j.core.message.MessageService;
import org.oopscraft.arch4j.core.variable.Variable;
import org.oopscraft.arch4j.core.variable.VariableSearch;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/message")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('PROPERTY')")
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
     * @param id message id
     * @return message
     */
    @GetMapping("get-message")
    @ResponseBody
    public Message getMessage(@RequestParam("id")String id) {
        return messageService.getMessage(id)
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    /**
     * saves message
     * @param message message info
     */
    @PostMapping("save-message")
    @ResponseBody
    public void saveMessage(@RequestBody @Valid Message message) {
        messageService.saveMessage(message);
    }

    /**
     * deletes message
     * @param id message id
     */
    @GetMapping("delete-message")
    @ResponseBody
    public void deleteMessage(@RequestParam("id")String id) {
        messageService.deleteMessage(id);
    }

}
