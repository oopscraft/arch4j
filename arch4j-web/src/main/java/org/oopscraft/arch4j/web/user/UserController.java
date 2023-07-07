package org.oopscraft.arch4j.web.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    /**
     * index
     * @return model and view
     */
    @GetMapping
    public ModelAndView user() {
        return new ModelAndView("user/user.html");
    }

}
