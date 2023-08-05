package org.oopscraft.arch4j.core.security;

import lombok.Builder;
import lombok.Getter;
import org.oopscraft.arch4j.core.role.Authority;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserStatus;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
public class UserDetailsImpl implements UserDetails, CredentialsContainer {

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

    public boolean hasRole(Role role) {
        GrantedAuthority authority = GrantedAuthorityImpl.from(role);
        return this.authorities.stream()
                .anyMatch(el -> Objects.equals(el.getAuthority(), authority.getAuthority()));
    }

    public boolean hasAuthority(Authority authority) {
        return this.authorities.stream()
                .anyMatch(el -> Objects.equals(el.getAuthority(), authority.getAuthorityId()));
    }

    public static UserDetailsImpl from(User user) {

        // adds authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(GrantedAuthorityImpl.from(role));
            role.getAuthorities().forEach(authority -> authorities.add(GrantedAuthorityImpl.from(authority)));
        });

        // build
        return UserDetailsImpl.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .accountNonLocked(user.getStatus() != UserStatus.LOCKED)
                .accountNonExpired(user.getStatus() != UserStatus.EXPIRED)
                .authorities(authorities)
                .build();
    }

}
