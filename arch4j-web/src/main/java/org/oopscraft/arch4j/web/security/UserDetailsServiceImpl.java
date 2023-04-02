package org.oopscraft.arch4j.web.security;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.repository.UserEntity;
import org.oopscraft.arch4j.core.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // retrieves user
        UserEntity user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));

        // creates user details
        UserDetailsImpl userDetails = new UserDetailsImpl(user.getId());
        userDetails.setPassword(user.getPassword());
        user.getRoles().forEach(role -> {
            userDetails.addAuthority(new GrantedAuthorityImpl("ROLE_" + role.getId()));
            role.getAuthorities().forEach(authority -> {
                userDetails.addAuthority(new GrantedAuthorityImpl(authority.getId()));
            });
        });

        // returns
        return userDetails;
    }

}
