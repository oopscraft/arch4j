package org.oopscraft.arch4j.web.error;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
public class ErrorResponseHandler {

    private final MessageSource messageSource;

    private final LocaleResolver localeResolver;

    private final ObjectMapper objectMapper;

    /**
     * isRestRequest
     * @param request request
     * @return result
     */
    public boolean isRestRequest(HttpServletRequest request) {
        if("application/json".equals(request.getHeader("Content-Type"))){
            return true;
        }
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-with"))){
            return true;
        }
        return false;
    }

    public ErrorResponse createErrorResponse(HttpServletRequest request, int status, Exception exception) {
        String messageId = String.format("web.error.%s", exception.getClass().getSimpleName());
        Locale locale = localeResolver.resolveLocale(request);
        String message = messageSource.getMessage(messageId, null, locale);
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .build();
    }

    public void sendRestErrorResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setHeader("Content-Type", "application/json");
        response.sendError(errorResponse.getStatus(), errorResponse.getMessage());
    }

    public void sendErrorResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.sendError(errorResponse.getStatus(), errorResponse.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public void handleNoSuchElementException(HttpServletRequest request, HttpServletResponse response, NoSuchElementException exception) throws IOException {
        int status = HttpStatus.NOT_FOUND.value();
        ErrorResponse errorResponse = createErrorResponse(request, status, exception);
        if(isRestRequest(request)) {
            sendRestErrorResponse(response, errorResponse);
        }else{
            sendErrorResponse(response, errorResponse);
        }
    }


}
