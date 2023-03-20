package org.oopscraft.arch4j.web.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AccessTokenService jwtEncoder;

    /**
     * doFilterInternal
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader( "Authorization" );
        if ( header != null && header.contains( "Bearer" ) ) {
            String[] tokens = header.split( " " );
            if ( tokens.length > 1 ) {
                String jwt = tokens[ 1 ];
                try {
                    UserDetailsImpl userDetails = jwtEncoder.decode(jwt);

                    // authenticates
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(authentication);

                } catch (Exception ignore) {
                    log.warn(ignore.getMessage());
                }
            }
        }
        filterChain.doFilter( request, response );

    }

}
