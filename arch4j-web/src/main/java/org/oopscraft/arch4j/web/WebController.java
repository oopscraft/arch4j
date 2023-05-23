package org.oopscraft.arch4j.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.oopscraft.arch4j.web.security.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@ControllerAdvice
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class WebController {

    private static final long SCRIPT_VERSION = System.currentTimeMillis();

    private final UserService userService;

    /**
     * FIXME index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        RedirectView redirectView = new RedirectView("/admin");
        redirectView.setExposeModelAttributes(false);
        modelAndView.setView(redirectView);
        //modelAndView.setViewName("_index.html");
        return modelAndView;
    }

    @ModelAttribute("_scriptVersion")
    public String scriptVersion() {
        return String.valueOf(SCRIPT_VERSION);
    }

    /**
     * Returns current login user
     * @return current login user info
     */
    @ModelAttribute("_user")
    @Transactional(readOnly = true)
    public User getUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if(authentication instanceof UsernamePasswordAuthenticationToken) {
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                return userService.getUser(userDetails.getUsername()).orElseThrow();
            }
        }
        return new User();
    }

}
