package org.chomoo.arch4j.core.common.pbe;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class PbeStringUtil implements ApplicationContextAware {

    private static StandardPBEStringEncryptor pbeStringEncryptor;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        pbeStringEncryptor = applicationContext.getBean(StandardPBEStringEncryptor.class);
    }

    /**
     * encrypt value
     * @param value origin value
     * @return encrypted value
     */
    public static String encrypt(String value) {
        return pbeStringEncryptor.encrypt(value);
    }

    /**
     * decrypt value
     * @param value encrypted value
     * @return origin value
     */
    public static String decrypt(String value) {
        return pbeStringEncryptor.decrypt(value);
    }

    /**
     * checks encrypted mark
     * @param value value
     * @return whether encrypted mark is included or not
     */
    public static boolean hasEncryptedMark(String value ) {
        if (value == null) {
            return false;
        }
        value = value.trim();
        return (value.startsWith("ENC(") && value.endsWith(")"));
    }

    /**
     * checks decrypted mark
     * @param value value
     * @return whether decrypted mark is included or not
     */
    public static boolean hasDecryptedMark(String value) {
        if (value == null) {
            return false;
        }
        value = value.trim();
        return (value.startsWith("DEC(") && value.endsWith(")"));
    }

    public static String wrapEncryptedMark(String value) {
        return "ENC(" + value + ")";
    }

    public static String wrapDecryptedMark(String value) {
        return "DEC(" + value + ")";
    }

    public static String unwrapEncryptedMark(String value) {
        if(hasEncryptedMark(value)) {
            return value.substring(
                    "ENC(".length(),
                    (value.length() - ")".length()));
        }
        return value;
    }

    public static String unwrapDecryptedMark(String value) {
        if(hasDecryptedMark(value)) {
            return value.substring(
                    "DEC(".length(),
                    (value.length() - ")".length()));
        }
        return value;
    }

    public static String encode(String value) {
        if(hasDecryptedMark(value)) {
            value = unwrapDecryptedMark(value);
            value = encrypt(value);
            return wrapEncryptedMark(value);
        }
        return value;
    }

    public static String decode(String value) {
        if(hasEncryptedMark(value)) {
            value = unwrapEncryptedMark(value);
            value = decrypt(value);
            return wrapDecryptedMark(value);
        }
        return value;
    }

}
