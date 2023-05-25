package org.oopscraft.arch4j.core.data.crpyto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component("defaultCryptoKeyProvider")
@ConditionalOnProperty(prefix = "core.crypto", name = "key-provider.bean", havingValue="defaultCryptoKeyProvider", matchIfMissing = true)
public class DefaultCryptoKeyProvider implements CryptoKeyProvider {

    @Value("${core.crypto.key-provider.password}")
    private String password;

    @Value("${core.crypto.key-provider.salt}")
    private String salt;

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getSalt() {
        return salt;
    }
}
