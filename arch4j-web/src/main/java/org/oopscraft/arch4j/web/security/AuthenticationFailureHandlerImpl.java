package org.oopscraft.arch4j.web.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.message.MessageSource;
import org.oopscraft.arch4j.web.security.exception.AuthenticationFailureException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * AuthenticationFailureHandler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    private final MessageSource messageSource;

    private final LocaleResolver localeResolver;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String messageId = String.format("web.error.%s", exception.getClass().getSimpleName());
        Locale locale = localeResolver.resolveLocale(request);
        String message = messageSource.getMessage(messageId, null, locale);
        throw new AuthenticationFailureException(message);
    }

}
