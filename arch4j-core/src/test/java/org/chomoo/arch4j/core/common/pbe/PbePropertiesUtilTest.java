package org.chomoo.arch4j.core.common.pbe;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.chomoo.arch4j.core.CoreConfiguration;
import org.chomoo.arch4j.core.common.test.CoreTestSupport;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreConfiguration.class)
@Slf4j
class PbePropertiesUtilTest extends CoreTestSupport {

    @Test
    void encodePropertiesString() {
        // given
        String originPropertiesString = "#comment\n" +
                "param1=DEC(value1)\n" +
                "param2=value2";
        // when
        String encodedPropertiesString = PbePropertiesUtil.encodePropertiesString(originPropertiesString);
        // then
        log.info("originPropertiesString:{}", originPropertiesString);
        log.info("encodedPropertiesString:{}", encodedPropertiesString);
    }

    @Test
    void loadPropertiesFromDecryptedMark() {
        // given
        String name = "param1";
        String originValue = "value1";
        String originPropertiesString = "#comment\n" +
                String.format("%s=DEC(%s)\n", name, originValue) +
                "param2=value2";
        // when
        Properties properties = PbePropertiesUtil.loadProperties(originPropertiesString);
        // then
        log.info("properties:{}", properties);
        assertEquals(originValue, properties.get(name));
    }

    @Test
    void loadPropertiesFromEncryptedValue() {
        // given
        String name = "param1";
        String originValue = "value1";
        String encryptedValue = PbeStringUtil.encrypt(originValue);
        String originPropertiesString = "#comment\n" +
                String.format("%s=ENC(%s)\n", name, encryptedValue) +
                "param2=value2";
        // when
        Properties properties = PbePropertiesUtil.loadProperties(originPropertiesString);
        // then
        log.info("properties:{}", properties);
        assertEquals(originValue, properties.get(name));
    }

}