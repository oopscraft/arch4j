package org.chomoo.arch4j.web.login.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("login/reset-password")
public class ResetPasswordController {

    @GetMapping
    public ModelAndView resetPassword() {
        return new ModelAndView("login/reset-password.html");
    }

}
