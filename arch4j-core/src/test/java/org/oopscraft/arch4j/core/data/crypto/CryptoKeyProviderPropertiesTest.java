package org.oopscraft.arch4j.core.data.crypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.oopscraft.arch4j.core.data.crpyto.CryptoKeyProviderProperties;
import org.oopscraft.arch4j.core.data.crpyto.CryptoUtil;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@RequiredArgsConstructor
class CryptoKeyProviderPropertiesTest extends CoreTestSupport {

    private final CryptoKeyProviderProperties cryptoKeyProviderProperties;

    @Test
    public void testDefault() {
        assertNotNull(cryptoKeyProviderProperties);
        assertNotNull(cryptoKeyProviderProperties.getBean());
        assertNotNull(cryptoKeyProviderProperties.getProperties());
    }

}