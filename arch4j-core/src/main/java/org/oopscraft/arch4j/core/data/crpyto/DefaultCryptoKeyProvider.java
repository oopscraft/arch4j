package org.oopscraft.arch4j.core.data.crpyto;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component("defaultCryptoKeyProvider")
@ConditionalOnProperty(prefix = "core.data.crypto.crypto-key-provider", name = "bean", havingValue="defaultCryptoKeyProvider", matchIfMissing = true)
@AllArgsConstructor
public class DefaultCryptoKeyProvider implements CryptoKeyProvider {

    private final CryptoKeyProviderProperties cryptoKeyProviderProperties;

    @Override
    public String getPassword() {
        return cryptoKeyProviderProperties.getProperties().getProperty("password");
    }

    @Override
    public String getSalt() {
        String salt = cryptoKeyProviderProperties.getProperties().getProperty("salt");
        return Hex.encodeHexString(salt.getBytes(StandardCharsets.UTF_8));
    }

}
