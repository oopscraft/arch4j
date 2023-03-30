package org.oopscraft.arch4j.core.data.crypto;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.nio.charset.StandardCharsets;

public class CryptoUtil {

    static TextEncryptor encryptor;

    static {
        encryptor = Encryptors.delux("This is password!", "0123456f");
    }

    public static String encrypt(String plainValue) {
        if(plainValue == null) {
            return null;
        }
        return encryptor.encrypt(plainValue);
    }

    public static String decrypt(String encryptedValue) {
        if(encryptedValue == null) {
            return null;
        }
        return encryptor.decrypt(encryptedValue);
    }

}
