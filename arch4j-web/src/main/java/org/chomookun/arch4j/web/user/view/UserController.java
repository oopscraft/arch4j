package org.chomookun.arch4j.web.user.view;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.security.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ModelAndView user() {
        ModelAndView modelAndView = new ModelAndView("user/user.html");
        modelAndView.addObject("userStatuses", User.Status.values());
        return modelAndView;
    }

}
