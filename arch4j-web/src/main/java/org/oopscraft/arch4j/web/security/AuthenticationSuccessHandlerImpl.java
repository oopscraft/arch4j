package org.oopscraft.arch4j.core.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.user.UserLogin;
import org.oopscraft.arch4j.core.user.UserLoginService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final UserLoginService loginHistoryService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // save login history
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
        LocalDateTime loginAt = LocalDateTime.now();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        UserLogin userLogin = UserLogin.builder()
                .userId(userId)
                .loginAt(loginAt)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();
        loginHistoryService.saveUserLogin(userLogin);
    }

}
