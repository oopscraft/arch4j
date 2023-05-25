package org.oopscraft.arch4j.web.login;

import lombok.Builder;
import org.oopscraft.arch4j.core.role.Authority;
import org.oopscraft.arch4j.core.role.Role;
import org.springframework.security.core.GrantedAuthority;

@Builder
public class GrantedAuthorityImpl implements GrantedAuthority {

    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }

    /**
     * factory method from Authority
     * @param authority authority
     * @return GrantedAuthorityImpl
     */
    public static GrantedAuthorityImpl from(Authority authority) {
        return GrantedAuthorityImpl.builder()
                .authority(authority.getId())
                .build();
    }

    /**
     * factory method from Role
     * @param role role
     * @return GrantedAuthorityImpl
     */
    public static GrantedAuthorityImpl from(Role role) {
        return GrantedAuthorityImpl.builder()
                .authority("ROLE_" + role.getId())
                .build();
    }

}
