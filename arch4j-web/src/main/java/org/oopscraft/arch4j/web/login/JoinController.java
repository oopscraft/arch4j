package org.oopscraft.arch4j.web.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("join")
@RequiredArgsConstructor
public class JoinController {

    /**
     * join
     * @return model and view
     */
    @GetMapping
    public ModelAndView join() {
        return new ModelAndView("join/join.html");
    }

}
