package org.oopscraft.arch4j.core.data.crypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.data.crpyto.CryptoKeyProviderProperties;
import org.oopscraft.arch4j.core.support.CoreTestSupport;

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