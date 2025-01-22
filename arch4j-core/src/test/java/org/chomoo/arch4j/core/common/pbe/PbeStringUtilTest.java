package org.chomoo.arch4j.core.common.pbe;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomoo.arch4j.core.CoreConfiguration;
import org.chomoo.arch4j.core.common.test.CoreTestSupport;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = CoreConfiguration.class)
class PbeStringUtilTest extends CoreTestSupport {

    @Test
    @Order(1)
    void encryptAndDecrypt() {
        // given
        String decryptedValue = "test";

        // when
        String encryptedValue = PbeStringUtil.encrypt(decryptedValue);
        log.debug("encryptedValue:" + encryptedValue);

        // then
        assertNotEquals(decryptedValue, encryptedValue);
        assertEquals(decryptedValue, PbeStringUtil.decrypt(encryptedValue));
    }

    @Test
    @Order(2)
    void encodeAndDecode() {
        // given
        String decodedValue = "DEC(test)";

        // when
        String encodedValue = PbeStringUtil.encode(decodedValue);
        log.debug("encodedValue:" + encodedValue);

        // then
        assertNotEquals(decodedValue, encodedValue);
        assertEquals(decodedValue, PbeStringUtil.decode(encodedValue));

    }

}