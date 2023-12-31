package org.oopscraft.arch4j.web.security;

import lombok.Builder;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.RoleService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public class SecurityFilter extends OncePerRequestFilter {

    private final PlatformTransactionManager transactionManager;

    private final SecurityTokenService authenticationTokenService;

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

                // keep alive
                securityToken = authenticationTokenService.encodeSecurityToken(userDetails);
                response.setHeader(HttpHeaders.AUTHORIZATION, securityToken);
            }
        }

        // creates new anonymous security token with anonymous authorities
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken anonymousAuthenticationToken) {
            List<GrantedAuthority> anonymousAuthorities = new ArrayList<>(anonymousAuthenticationToken.getAuthorities());
            anonymousAuthorities.addAll(getAnonymousAuthorities());
            securityContext.setAuthentication(new AnonymousAuthenticationToken(
                    UUID.randomUUID().toString(),
                    anonymousAuthenticationToken.getPrincipal(),
                    anonymousAuthorities));
        }

        // forward
        filterChain.doFilter(request, response);
    }

    public Collection<GrantedAuthority> getAnonymousAuthorities() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(Propagation.REQUIRED.value());
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, transactionDefinition);
        return transactionTemplate.execute(status -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            // getting anonymous role
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
