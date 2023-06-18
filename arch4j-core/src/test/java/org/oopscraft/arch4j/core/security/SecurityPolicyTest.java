package org.oopscraft.arch4j.core.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SecurityPolicyTest {

    @Test
    public void test() {
        log.info("== name:{}", SecurityPolicy.AUTHENTICATED.name());
        log.info("== label:{}", SecurityPolicy.AUTHENTICATED.getLabel());
    }

}