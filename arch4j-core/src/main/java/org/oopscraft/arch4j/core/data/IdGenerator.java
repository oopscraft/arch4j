package org.oopscraft.arch4j.core.data;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * ID generator helper
 */
@Slf4j
public class IdGenerator {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * generates UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * encode base64
     */
    public static String encodeBase64(String plainValue) {
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
        if(encodedValue == null) {
            return null;
        }
        byte[] bytes = Base64.getUrlDecoder().decode(encodedValue.getBytes(CHARSET));
        String decodedValue = new String(bytes, CHARSET);
        log.debug("decodedValue:{}", decodedValue);
        return decodedValue;
    }

}