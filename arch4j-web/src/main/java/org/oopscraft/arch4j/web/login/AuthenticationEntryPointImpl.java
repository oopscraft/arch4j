package org.oopscraft.arch4j.web.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final MessageSource messageSource;

    private final LocaleResolver localeResolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        if("application/json".equals(request.getHeader("Content-Type"))
        || "XMLHttpRequest".equals(request.getHeader("X-Requested-with"))
        ){
            response.setHeader("Content-Type", "application/json");
            String message = messageSource.getMessage("admin.security.AuthenticationException", null, localeResolver.resolveLocale(request));
            response.sendError(HttpServletResponse.SC_FORBIDDEN, HtmlUtils.htmlEscape(message));
        }else{
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", LoginConstant.LOGIN_URL);
			response.getWriter().flush();
        }
    }


}
