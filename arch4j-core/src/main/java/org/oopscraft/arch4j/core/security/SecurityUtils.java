package org.oopscraft.arch4j.core.security;

import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {

    private static UserService userService;

    @Autowired
    public void setUserService(UserService userServiceBean){
        userService = userServiceBean;
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
    public static Optional<String> getCurrentUserId() {
        String userId = getUserDetails()
                .map(UserDetailsImpl::getUsername)
                .orElse(null);
        return Optional.ofNullable(userId);
    }

}
