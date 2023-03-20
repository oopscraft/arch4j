package org.oopscraft.arch4j.web.security;

import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class UserDetailsImpl implements UserDetails, CredentialsContainer {

    private String username;

    @Setter
    private String password;

    @Setter
    private boolean accountNonLocked = true;

    @Setter
    private boolean accountNonExpired = true;

    @Setter
    private boolean credentialNonExpired = true;

    private Collection<GrantedAuthority> authorities = new ArrayList<>();

    /**
     * constructor
     * @param username
     */
    public UserDetailsImpl(String username) {
        this.username = username;
    }

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

    /**
     * addAuthority
     * @param authority
     */
    public void addAuthority(GrantedAuthority authority) {
        this.authorities.add(authority);
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

}
