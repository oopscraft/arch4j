package org.oopscraft.arch4j.core.security;

import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SecurityUtils {

    private static UserService userService;

    @Autowired
    public void setUserService(UserService userServiceBean){
        userService = userServiceBean;
    }

    /**
     * check if authenticated
     * @return whether authenticated or not
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            return authentication instanceof UsernamePasswordAuthenticationToken;
        }
        return false;
    }

    /**
     * returns current login user details
     * @return user details
     */
    public static Optional<UserDetailsImpl> getUserDetails() {
        UserDetailsImpl userDetails = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if(authentication instanceof UsernamePasswordAuthenticationToken) {
                userDetails = (UserDetailsImpl) authentication.getPrincipal();
            }
        }
        return Optional.ofNullable(userDetails);
    }

    /**
     * returns current login user info
     * @return user info
     */
    public static Optional<User> getCurrentUser() {
        User user = getUserDetails()
                .flatMap(userDetails -> userService.getUser(userDetails.getUsername()))
                .orElse(null);
        return Optional.ofNullable(user);
    }

    /**
     * return current login user id
     * @return user id
     */
    public static String getCurrentUserId() {
        return getUserDetails()
                .map(UserDetailsImpl::getUsername)
                .orElse(null);
    }

    /**
     * check has any role
     * @param roles roles
     * @return result
     */
    public static boolean hasAnyRole(List<Role> roles) {
        UserDetailsImpl userDetails = getUserDetails().orElse(null);
        if(userDetails == null) {
            return false;
        }
        return roles.stream().anyMatch(userDetails::hasRole);
    }

    /**
     * has permission
     * @param securityPolicy security policy
     * @param requiredRoles roles
     * @return result
     */
    @Deprecated
    public static boolean hasPermission(SecurityPolicy securityPolicy, List<Role> requiredRoles) {
        if(securityPolicy == SecurityPolicy.AUTHENTICATED) {
            return isAuthenticated();
        }
        if(securityPolicy == SecurityPolicy.AUTHORIZED) {
            return hasAnyRole(requiredRoles);
        }
        return true;
    }


    /**
     * check has permission
     * @param targetSecurityPolicy target security policy
     * @param targetRequiredRoles target required roles
     */
    public static void checkPermission(SecurityPolicy targetSecurityPolicy, List<Role> targetRequiredRoles) {
        if(targetSecurityPolicy == SecurityPolicy.AUTHENTICATED) {
            if(!isAuthenticated()) {
                throw new InsufficientAuthenticationException("required authentication");
            }else{
                return;
            }
        }
        if(targetSecurityPolicy == SecurityPolicy.AUTHORIZED) {
            if(!hasAnyRole(targetRequiredRoles)) {
                throw new AccessDeniedException("access denied");
            }
        }
    }

}
