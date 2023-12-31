package org.oopscraft.arch4j.web.user.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.UserStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("user/change-password")
@RequiredArgsConstructor
public class ChangePasswordController {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ModelAndView changePassword() {
        return new ModelAndView("user/change-password.html");
    }

}
