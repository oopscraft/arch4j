package org.oopscraft.arch4j.web;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@ControllerAdvice
@RequiredArgsConstructor
public class WebControllerAdvice {

    private static final long SCRIPT_VERSION = System.currentTimeMillis();

    private final UserService userService;

    @ModelAttribute("_scriptVersion")
    public String scriptVersion() {
        return String.valueOf(SCRIPT_VERSION);
    }

    @ModelAttribute("_apiVersion")
    public String apiVersion() {
        return "v1";
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
        return SecurityUtils.getCurrentUser()
                .orElse(new User());
    }

}
