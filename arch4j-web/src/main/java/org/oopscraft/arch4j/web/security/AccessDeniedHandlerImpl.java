package org.oopscraft.arch4j.web.security;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.web.error.ErrorResponse;
import org.oopscraft.arch4j.web.error.ErrorResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final ErrorResponseHandler errorResponseHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResponse errorResponse = errorResponseHandler.createErrorResponse(request, HttpStatus.FORBIDDEN, accessDeniedException);
        if (errorResponseHandler.isRestRequest(request)) {
            errorResponseHandler.sendRestErrorResponse(response, errorResponse);
        }else{
            errorResponseHandler.sendErrorResponse(response, errorResponse);
        }
    }

}
