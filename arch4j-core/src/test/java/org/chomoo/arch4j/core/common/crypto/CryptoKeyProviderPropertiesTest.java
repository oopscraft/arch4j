package org.chomoo.arch4j.core.common.crypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.chomoo.arch4j.core.common.crpyto.CryptoKeyProviderProperties;
import org.chomoo.arch4j.core.common.test.CoreTestSupport;

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