package org.chomoo.arch4j.web.security.service;

import lombok.RequiredArgsConstructor;
import org.chomoo.arch4j.core.security.service.AuthorityService;
import org.chomoo.arch4j.core.security.model.Role;
import org.chomoo.arch4j.core.security.service.RoleService;
import org.chomoo.arch4j.core.security.model.User;
import org.chomoo.arch4j.core.security.service.UserService;
import org.chomoo.arch4j.web.security.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // retrieves by username
        User user = userService.getUserByUsername(username).orElseThrow(()->{
            throw new UsernameNotFoundException(username);
        });

        // user details
        UserDetailsImpl userDetails =  UserDetailsImpl.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
                .admin(user.isAdmin())
                .build();

        // checks disabled
        if(user.getStatus() == User.Status.CLOSED) {
            userDetails.setEnabled(false);
        }

        // checks account locked
        if(user.getStatus() == User.Status.LOCKED) {
            userDetails.setAccountNonLocked(false);
        }

        // checks account expired
        if(user.getExpireAt() != null) {
            if(user.getExpireAt().isAfter(Instant.now())) {
                userDetails.setAccountNonExpired(false);
            }
        }

        // add user roles
        userDetails.addRoles(user.getRoles());

        // roles
        final List<Role> roles = roleService.getRoles();

        // add anonymous roles
        userDetails.addRoles(roles.stream()
                .filter(Role::isAnonymous)
                .collect(Collectors.toList()));

        // add authenticated roles
        userDetails.addRoles(roles.stream()
                .filter(Role::isAuthenticated)
                .collect(Collectors.toList()));

        // in case of admin, add all roles and authorities
        if(user.isAdmin()) {
            userDetails.addRoles(roles);
            userDetails.addAuthorities(authorityService.getAuthorities());
        }

        return userDetails;
    }

}
