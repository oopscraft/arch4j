package org.oopscraft.arch4j.core.security;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.support.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class AuthenticationTokenPropertiesTest extends CoreTestSupport {

    private final AuthenticationTokenProperties authenticationTokenProperties;

    @Test
    public void testLoad() {
        assertNotNull(authenticationTokenProperties);
        assertNotNull(authenticationTokenProperties.getSigningKey());
        assertNotNull(authenticationTokenProperties.getExpireMinutes());
    }

}