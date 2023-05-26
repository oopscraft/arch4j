package org.oopscraft.arch4j.web;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.oopscraft.arch4j.web.security.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RequiredArgsConstructor
public class WebControllerAdvice {

    private static final long SCRIPT_VERSION = System.currentTimeMillis();

    private final UserService userService;

    @ModelAttribute("_scriptVersion")
    public String scriptVersion() {
        return String.valueOf(SCRIPT_VERSION);
    }

    @ModelAttribute("_theme")
    public String theme() {
        return "_default";
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
