package org.oopscraft.arch4j.web.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("login/join")
@RequiredArgsConstructor
public class JoinController {

    /**
     * index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("login/join.html");
    }

    @PostMapping("send-verification-code")
    public void sendVerificationCode(@RequestParam("email")String email) {

    }

}
