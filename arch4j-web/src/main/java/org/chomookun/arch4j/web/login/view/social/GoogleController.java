package org.chomookun.arch4j.web.login.view.social;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("user/login/social/google")
public class GoogleController {

    @GetMapping
    public void index(HttpServletResponse response) throws IOException {
        String redirectUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=372120948172-9iioibgeotln9303ic988kb57nsr8shp.apps.googleusercontent.com&redirect_uri=http://localhost:8080/google-login&response_type=code&scope=email profile";
        response.sendRedirect(redirectUrl);
    }

}
