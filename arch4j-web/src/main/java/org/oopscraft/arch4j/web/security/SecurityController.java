package org.oopscraft.arch4j.web.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/security")
public class SecurityController {

    /**
     * login
     * @param request
     * @param error
     * @return
     */
    @GetMapping("login")
    public ModelAndView login(HttpServletRequest request,@RequestParam(value = "error", required = false, defaultValue = "false") Boolean error) {
        ModelAndView modelAndView = new ModelAndView("security/login.html");

        // error
        if(error == true) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                AuthenticationException exception = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                modelAndView.addObject("errorMessage", exception.getMessage());
            }
        }

        // returns
        return modelAndView;
    }

}
