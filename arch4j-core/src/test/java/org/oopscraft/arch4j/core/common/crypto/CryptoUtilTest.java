package org.oopscraft.arch4j.core.common.crypto;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.oopscraft.arch4j.core.common.crpyto.CryptoUtil;
import org.oopscraft.arch4j.core.common.test.CoreTestSupport;

import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CryptoUtilTest extends CoreTestSupport {

    @ParameterizedTest
    @ValueSource(strings = {"en","ko","ja","zh-CN"})
    public void testDefault(String language) {
        for(int i = 0; i < 1000; i ++ ) {
            Faker faker = new Faker(new Locale(language), new Random(i));
            String plainValue = faker.name().fullName();
            log.debug("== plainValue:{}", plainValue);
            String encryptedValue = CryptoUtil.getInstance().encrypt(plainValue);
            log.debug("== encryptedValue:{}", encryptedValue);
            String decryptedValue = CryptoUtil.getInstance().decrypt(encryptedValue);
            log.debug("== decryptedValue:{}", decryptedValue);
            assertEquals(plainValue, decryptedValue, "decrypted value is not match plain value");
        }
    }

}