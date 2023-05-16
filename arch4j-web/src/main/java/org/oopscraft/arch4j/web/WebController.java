package org.oopscraft.arch4j.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.oopscraft.arch4j.web.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@ControllerAdvice
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class WebController {

    private final UserService userService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("_index.html");
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
                User user = userService.getUser(userDetails.getUsername()).orElseThrow();
                log.debug("== user: {}", user);
                return user;
            }
        }
        return new User();
    }

}
