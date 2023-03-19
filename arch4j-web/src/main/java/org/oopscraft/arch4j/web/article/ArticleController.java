package org.oopscraft.arch4j.web.article;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ArticleController {

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("job.html");
        return modelAndView;
    }

}
