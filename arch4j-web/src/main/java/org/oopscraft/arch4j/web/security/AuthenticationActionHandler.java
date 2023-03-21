package org.oopscraft.arch4j.web.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.web.security.exception.AuthenticationFailureException;
import org.oopscraft.arch4j.web.security.exception.AuthorizationFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationActionHandler implements AuthenticationEntryPoint, AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler, AccessDeniedHandler {

    public enum ResponseType {
        VIEW, REST
    }

    private final ResponseType responseType;

    /**
     * isRestRequest
     * @return
     */
    private boolean isRestRequest(HttpServletRequest request) {
        if (responseType == ResponseType.REST
        || "application/json".equals(request.getHeader("Content-Type"))
        || "XMLHttpRequest".equals(request.getHeader("X-Requested-with"))
        ){
            return true;
        }else{
            return false;
        }
    }

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException {
        if(isRestRequest(request)){
            response.setStatus(HttpServletResponse.SC_OK);
        }else{
            redirect(response, "/");
        }
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String message = exception.getMessage();
        if(exception instanceof AuthenticationException) {
            throw new AuthorizationFailureException(message);
        }else{
            throw exception;
        }
	}
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(isRestRequest(request)){
            response.setStatus(HttpServletResponse.SC_OK);
        }else{
            redirect(response, "/");
        }
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if(isRestRequest(request)){
            String message = authException.getMessage();
            throw new AuthenticationFailureException(message);
        }
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String message = accessDeniedException.getMessage();
        throw new AuthorizationFailureException(message);
    }

    /**
     * redirect
     * @param path
     */
    private void redirect(HttpServletResponse response, String path) {
        try {
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", path);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
