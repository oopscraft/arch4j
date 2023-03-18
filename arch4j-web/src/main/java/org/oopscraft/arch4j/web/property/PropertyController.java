package org.oopscraft.arch4j.web.property;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("property")
public class PropertyController {

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("property/property.html");
    }

}
