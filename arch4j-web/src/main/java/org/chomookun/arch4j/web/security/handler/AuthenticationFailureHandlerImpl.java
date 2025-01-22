package org.chomookun.arch4j.web.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.web.common.error.ErrorResponse;
import org.chomookun.arch4j.web.common.error.ErrorResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    private final ErrorResponseHandler errorResponseHandler;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        // login/process request is POST method, but response is only application/json content type.
        ErrorResponse errorResponse = errorResponseHandler.createErrorResponse(request, HttpStatus.UNAUTHORIZED, exception);
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setStatus(errorResponse.getStatus());
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

}
