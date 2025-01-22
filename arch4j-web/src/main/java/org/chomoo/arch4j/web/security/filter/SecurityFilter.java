package org.chomoo.arch4j.web.security.filter;

import lombok.Builder;
import org.chomoo.arch4j.core.security.model.Role;
import org.chomoo.arch4j.core.security.service.RoleService;
import org.chomoo.arch4j.web.security.model.GrantedAuthorityImpl;
import org.chomoo.arch4j.web.security.service.SecurityTokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Builder
public class SecurityFilter extends OncePerRequestFilter {

    private final PlatformTransactionManager transactionManager;

    private final SecurityTokenService authenticationTokenService;

    private final UserDetailsService userDetailsService;

    private final RoleService roleService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // check security token
        String securityToken = null;
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization != null && authorization.trim().length() > 0) {
            String[] authorizationArray = authorization.split(" ");
            if(authorizationArray.length >= 2) {
                securityToken = authorizationArray[1];
            }

            // if security token exist
            if(securityToken != null) {
                UserDetails userDetails = authenticationTokenService.decodeSecurityToken(securityToken);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
            }
        }

        // checks authentication
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        // creates and updates anonymous security token with anonymous authorities
        if(authentication instanceof AnonymousAuthenticationToken anonymousAuthenticationToken) {
            List<GrantedAuthority> anonymousAuthorities = new ArrayList<>(anonymousAuthenticationToken.getAuthorities());
            anonymousAuthorities.addAll(getAnonymousAuthorities());
            securityContext.setAuthentication(new AnonymousAuthenticationToken(
                    UUID.randomUUID().toString(),
                    anonymousAuthenticationToken.getPrincipal(),
                    anonymousAuthorities));
        }

        // creates and updates user details
        if(authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
            UserDetails userDetails = (UserDetails) usernamePasswordAuthenticationToken.getPrincipal();
            userDetails = userDetailsService.loadUserByUsername(userDetails.getUsername());
            usernamePasswordAuthenticationToken.setDetails(userDetails);
        }

        // forward
        filterChain.doFilter(request, response);
    }

    /**
     * gets anonymous authorities
     * @return anonymous authorities
     */
    public Collection<GrantedAuthority> getAnonymousAuthorities() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(Propagation.REQUIRED.value());
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, transactionDefinition);
        return transactionTemplate.execute(status -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            List<Role> anonymousRoles = roleService.getRoles().stream()
                    .filter(Role::isAnonymous)
                    .toList();
            anonymousRoles.forEach(role -> {
                authorities.add(GrantedAuthorityImpl.from(role));
                role.getAuthorities().forEach(authority -> {
                    authorities.add(GrantedAuthorityImpl.from(authority));
                });
            });
            return authorities;
        });
    }

}
