package org.oopscraft.arch4j.web.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;


public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler, AuthenticationEntryPoint, AccessDeniedHandler {

    private final AccessTokenService accessTokenService;

    public enum ResponseType {
        VIEW, REST
    }

    private ResponseType responseType;

    /**
     * constructor
     * @param accessTokenService
     */
    public AuthenticationHandler(ResponseType responseType, AccessTokenService accessTokenService){
        this.responseType = responseType;
        this.accessTokenService = accessTokenService;
    }

    /**
     * returns is rest request
     * @param request
     * @return
     */
    private boolean isRestRequest(HttpServletRequest request) {
        if(responseType ==  ResponseType.REST
        || "application/json".equals(request.getHeader("Content-Type"))
        || "XMLHttpRequest".equals(request.getHeader("X-Requested-with"))
        ) {
            return true;
        }else{
            return false;
        }
    }

    /**
     * AuthenticationSuccessHandler.doAuthenticationSuccess
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
            accessTokenService.issueAccessToken(userDetails, response);
            if(isRestRequest(request)){
                response.setStatus(HttpServletResponse.SC_OK);
            }else{
                String referer = request.getHeader("referer");
                if(referer != null){
                    response.sendRedirect(referer);
                }else{
                    response.sendRedirect("/");
                }
            }
        }catch(Throwable t) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, HtmlUtils.htmlEscape(t.getMessage()));
        }
    }

    /**
     * AuthenticationFailureHandler.onAuthenticationFailure
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof AuthenticationException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
        }else{
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    /**
     * LogoutSuccessHandler.onLogoutSuccess
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    }

    /**
     * AuthenticationEntryPoint.commence
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

    }

    /**
     * AccessDenyHandler.handle
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

    }
}
