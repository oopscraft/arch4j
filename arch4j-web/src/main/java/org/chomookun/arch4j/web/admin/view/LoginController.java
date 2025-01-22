package org.chomookun.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/login")
@RequiredArgsConstructor
public class LoginController {

    @GetMapping
    public ModelAndView login() {
        return new ModelAndView("admin/login.html");
    }

}
