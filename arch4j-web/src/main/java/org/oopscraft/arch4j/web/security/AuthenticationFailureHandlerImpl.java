package org.oopscraft.arch4j.web.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.web.error.ErrorResponseHandler;
import org.oopscraft.arch4j.web.error.dto.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    private final ErrorResponseHandler errorResponseHandler;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        ErrorResponse errorResponse = errorResponseHandler.createErrorResponse(request, status, exception);
        errorResponseHandler.sendRestErrorResponse(response, errorResponse);
    }

}
