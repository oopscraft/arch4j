package org.oopscraft.arch4j.core.support;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.UUID;

/**
 * ID generator helper
 */
@Slf4j
public class IdGenerator {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * generates UUID
     */
    public static String uuid() {
        String uuid = UUID.randomUUID().toString();
        log.debug("uuid:{}", uuid);
        return uuid;
    }

    /**
     * encode base64
     */
    public static String encodeBase64(String plainValue) {
        log.debug("plainValue:{}", plainValue);
        if(plainValue == null) {
            return null;
        }
        byte[] bytes = Base64.getUrlEncoder().encode(plainValue.getBytes(CHARSET));
        String encodedValue = new String(bytes, CHARSET);
        log.debug("encodedValue:{}", encodedValue);
        return encodedValue;
    }

    /**
     * decode base64
     */
    public static String decodeBase64(String encodedValue) {
        log.debug("encodedValue:{}", encodedValue);
        if(encodedValue == null) {
            return null;
        }
        byte[] bytes = Base64.getUrlDecoder().decode(encodedValue.getBytes(CHARSET));
        String decodedValue = new String(bytes, CHARSET);
        log.debug("decodedValue:{}", decodedValue);
        return decodedValue;
    }

}