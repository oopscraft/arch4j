package org.oopscraft.arch4j.web.login;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final MessageSource messageSource;

    private final LocaleResolver localeResolver;


    /**
     * login
     * @param request
     * @param error
     * @return
     */
    @GetMapping
    public ModelAndView login(HttpServletRequest request,@RequestParam(value = "error", required = false, defaultValue = "false") Boolean error) {
        ModelAndView modelAndView = new ModelAndView("login/login.html");

        // error
        if(error) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                AuthenticationException exception = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                // converts message
                String errorMessage = messageSource.getMessage(
                        String.format("web.error.%s",exception.getClass().getSimpleName()),
                        null,
                        localeResolver.resolveLocale(request)
                );
                modelAndView.addObject("errorMessage", errorMessage);
            }
        }

        // returns
        return modelAndView;
    }

}
