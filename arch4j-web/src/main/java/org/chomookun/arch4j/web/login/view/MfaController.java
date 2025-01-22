package org.chomookun.arch4j.web.login.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * TODO
 */
@Controller
@RequestMapping("mfa")
public class MfaController {

    @PostMapping("process")
    public ModelAndView process(@RequestParam("mfa_code") String mfaCode, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

}
