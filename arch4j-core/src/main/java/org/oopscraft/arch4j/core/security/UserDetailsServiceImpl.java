package org.oopscraft.arch4j.core.security;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // retrieves by username
        User user = userService.getUser(username).orElseThrow(()->{
            throw new UsernameNotFoundException(username);
        });

        // return user details
        return UserDetailsImpl.from(user);
    }

}
