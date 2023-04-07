package org.oopscraft.arch4j.web.security;

import lombok.Builder;
import lombok.Getter;
import org.oopscraft.arch4j.core.user.User;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Builder
public class UserDetailsImpl implements UserDetails, CredentialsContainer {

    private String id;

    private String username;

    private String password;

    @Builder.Default
    private Boolean accountNonLocked = true;

    @Builder.Default
    private Boolean accountNonExpired = true;

    @Builder.Default
    private Boolean credentialNonExpired = true;

    @Builder.Default
    private Collection<GrantedAuthority> authorities = new ArrayList<>();

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialNonExpired;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    /**
     * factory method
     * @param user user
     * @return userDetailsImpl
     */
    public static UserDetailsImpl from(User user) {

        // adds authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(GrantedAuthorityImpl.from(role));
            role.getAuthorities().forEach(authority -> {
                authorities.add(GrantedAuthorityImpl.from(authority));
            });
        });

        // build
        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getName())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

}
