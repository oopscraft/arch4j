package org.chomoo.arch4j.web.security.handler;

import lombok.RequiredArgsConstructor;
import org.chomoo.arch4j.web.common.error.ErrorResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final ErrorResponseHandler errorResponseHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        errorResponseHandler.sendErrorResponse(request, response, HttpStatus.FORBIDDEN, accessDeniedException);
    }

}
