package org.oopscraft.arch4j.core.security;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.error.ErrorResponse;
import org.oopscraft.arch4j.core.error.ErrorResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final ErrorResponseHandler errorResponseHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        ErrorResponse errorResponse = errorResponseHandler.createErrorResponse(request, HttpStatus.FORBIDDEN, authenticationException);
        if (errorResponseHandler.isRestRequest(request)) {
            errorResponseHandler.sendRestErrorResponse(response, errorResponse);
        }else{
            String requestUri = request.getRequestURI();
            String loginUrl;
            if(requestUri.startsWith("/admin")) {
                loginUrl = "/admin/login";
            }else{
                loginUrl = "/login";
            }
            response.sendRedirect(loginUrl);
        }
    }

}
