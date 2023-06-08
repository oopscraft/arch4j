package org.oopscraft.arch4j.web.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.security.AuthenticationTokenService;
import org.oopscraft.arch4j.core.user.LoginHistory;
import org.oopscraft.arch4j.core.user.LoginHistoryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * AuthenticationSuccessHandler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final AuthenticationTokenService authenticationTokenService;

    private final LoginHistoryService loginHistoryService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // issue authentication token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String authenticationToken = authenticationTokenService.encodeAuthenticationToken(userDetails);
        authenticationTokenService.issueAuthenticationToken(response, authenticationToken);

        // save login history
        String userId = userDetails.getUsername();
        LocalDateTime loginDateTime = LocalDateTime.now();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        LoginHistory loginHistory = LoginHistory.builder()
                .userId(userId)
                .loginDateTime(loginDateTime)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();
        loginHistory = loginHistoryService.saveLoginHistory(loginHistory);
        log.debug("loginHistory:{}", loginHistory);
    }

}
