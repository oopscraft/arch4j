package org.oopscraft.arch4j.web.common.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class ErrorResponseHandler {

    private final MessageSource messageSource;

    private final LocaleResolver localeResolver;

    private final ObjectMapper objectMapper;

    /**
     * sends error response
     * @param request http servlet request
     * @param response http servlet response
     * @param status http status
     * @param exception exception
     */
    public void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus status, Exception exception) {
        ErrorResponse errorResponse = createErrorResponse(request, status, exception);
        try {
            if (isRestRequest(request)) {
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.setStatus(errorResponse.getStatus());
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                response.getWriter().write(jsonResponse);
            } else {
                response.sendError(errorResponse.getStatus(), errorResponse.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * creates error response
     * @param request http servlet request
     * @param status http status
     * @param exception exception
     * @return error response
     */
    public ErrorResponse createErrorResponse(HttpServletRequest request, HttpStatus status, Exception exception) {
        String messageId = String.format("web.error.%s", exception.getClass().getSimpleName());
        Locale locale = localeResolver.resolveLocale(request);
        String message = messageSource.getMessage(messageId, null, locale);
        return ErrorResponse.from(request, status, message);
    }

    /**
     * checks is rest request
     * @param request http servlet request
     * @return whether if rest request or not
     */
    public boolean isRestRequest(HttpServletRequest request) {
        if ("application/json".equals(request.getHeader("Content-Type"))){
            return true;
        }
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-with"))){
            return true;
        }
        return false;
    }

}
