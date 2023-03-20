package org.oopscraft.arch4j.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class WebController {

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("_index.html");
    }

}
