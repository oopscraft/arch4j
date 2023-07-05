package org.oopscraft.arch4j.web.security;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.web.error.ErrorResponseHandler;
import org.oopscraft.arch4j.web.error.dto.ErrorResponse;
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
        int status = HttpServletResponse.SC_FORBIDDEN;
        ErrorResponse errorResponse = errorResponseHandler.createErrorResponse(request, status, authenticationException);
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
