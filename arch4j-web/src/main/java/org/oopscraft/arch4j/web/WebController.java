package org.oopscraft.arch4j.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.security.SecurityPolicy;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WebController {

    private final WebProperties webProperties;

    /**
     * FIXME index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        // anonymous access allowed
        if(webProperties.getSecurityPolicy() == SecurityPolicy.ANONYMOUS) {
            return new ModelAndView("web.html");
        }
        // authenticated, authorized
        else {
            // already login user
            if (SecurityUtils.isAuthenticated()){
                return new ModelAndView("web.html");
            }
            // not login user
            else{
                RedirectView redirectView = new RedirectView("/login");
                return new ModelAndView(redirectView);
            }
        }
    }

}
