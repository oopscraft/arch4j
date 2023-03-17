package org.oopscraft.app4j.web.forum;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ForumController {

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("job.html");
        return modelAndView;
    }

}
