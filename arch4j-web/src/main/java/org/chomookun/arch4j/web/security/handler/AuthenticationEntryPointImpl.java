package org.chomookun.arch4j.web.security.handler;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.web.common.error.ErrorResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final ErrorResponseHandler errorResponseHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        if (errorResponseHandler.isRestRequest(request)) {
            errorResponseHandler.sendErrorResponse(request, response, HttpStatus.FORBIDDEN, authenticationException);
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
