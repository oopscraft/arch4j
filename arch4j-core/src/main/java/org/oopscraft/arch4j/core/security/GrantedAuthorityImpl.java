package org.oopscraft.arch4j.core.security;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.oopscraft.arch4j.core.role.Authority;
import org.oopscraft.arch4j.core.role.Role;
import org.springframework.security.core.GrantedAuthority;

@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GrantedAuthorityImpl implements GrantedAuthority {

    @EqualsAndHashCode.Include
    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public static GrantedAuthorityImpl from(Authority authority) {
        return GrantedAuthorityImpl.builder()
                .authority(authority.getAuthorityId())
                .build();
    }

    public static GrantedAuthorityImpl from(Role role) {
        return GrantedAuthorityImpl.builder()
                .authority("ROLE_" + role.getRoleId())
                .build();
    }

}
