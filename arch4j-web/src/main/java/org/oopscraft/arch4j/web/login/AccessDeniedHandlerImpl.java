package org.oopscraft.arch4j.web.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final MessageSource messageSource;

    private final LocaleResolver localeResolver;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String message = messageSource.getMessage("admin.security.AccessDeniedException", null, localeResolver.resolveLocale(request));
        if("application/json".equals(request.getHeader("Content-Type"))
        || "XMLHttpRequest".equals(request.getHeader("X-Requested-with"))
        ){
            response.setHeader("Content-Type", "application/json");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, HtmlUtils.htmlEscape(message));
        }else{
            response.sendError(HttpServletResponse.SC_FORBIDDEN, HtmlUtils.htmlEscape(message));
        }

    }

}
