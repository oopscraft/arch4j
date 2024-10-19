package org.oopscraft.arch4j.core.common.pbe;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.CoreConfiguration;
import org.oopscraft.arch4j.core.common.test.CoreTestSupport;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreConfiguration.class)
@Slf4j
class PbePropertiesUtilTest extends CoreTestSupport {

    @Test
    void encode() {
        // given
        String properties = "#comment\n" +
                "param1=DEC(value1)\n" +
                "param2=value2";
        // when
        String encodedProperties = PbePropertiesUtil.encode(properties);
        // then
        log.info("encodedProperties:{}", encodedProperties);
    }

}