package org.chomoo.arch4j.web.join.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("join")
@RequiredArgsConstructor
public class JoinController {

    @GetMapping
    public ModelAndView join() {
        return new ModelAndView("join/join.html");
    }

}
