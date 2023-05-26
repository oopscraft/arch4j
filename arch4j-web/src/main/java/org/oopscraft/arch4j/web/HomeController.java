package org.oopscraft.arch4j.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private static final long SCRIPT_VERSION = System.currentTimeMillis();

    private final UserService userService;

    /**
     * FIXME index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();

        // TODO
        RedirectView redirectView = new RedirectView("/admin");
        redirectView.setExposeModelAttributes(false);
        modelAndView.setView(redirectView);
        //modelAndView.setViewName("home.html");
        return modelAndView;
    }

}
