package org.oopscraft.arch4j.web.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.user.LoginHistory;
import org.oopscraft.arch4j.core.user.LoginHistoryService;
import org.oopscraft.arch4j.core.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
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

    private final LoginHistoryService loginHistoryService;

    private final RequestCache requestCache = new HttpSessionRequestCache();

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // save login history
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
        LocalDateTime loginDateTime = LocalDateTime.now();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        LoginHistory loginHistory = LoginHistory.builder()
                .userId(userId)
                .loginDateTime(loginDateTime)
                .loginSuccess(true)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();
        loginHistoryService.saveLoginHistory(loginHistory);

        // redirect
        String redirectUrl = "/";
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
        }
        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }

}
