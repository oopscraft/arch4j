package org.oopscraft.arch4j.web.security;

import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {

    private String authority;

    /**
     * constructor
     * @param authority
     */
    public GrantedAuthorityImpl(String authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

}
