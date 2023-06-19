package org.oopscraft.arch4j.core.data.crpyto;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

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
        return cryptoKeyProviderProperties.getProperties().getProperty("salt");
    }

}
