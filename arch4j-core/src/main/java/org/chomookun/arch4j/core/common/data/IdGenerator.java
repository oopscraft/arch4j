package org.chomookun.arch4j.core.common.data;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Slf4j
public class IdGenerator {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static String md5(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, messageDigest);
            StringBuilder md5Hash = new StringBuilder(number.toString(16));
            while (md5Hash.length() < 32) {
                md5Hash.insert(0, '0');
            }
            return md5Hash.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeBase64(String plainValue) {
        if(plainValue == null) {
            return null;
        }
        byte[] bytes = Base64.getUrlEncoder().encode(plainValue.getBytes(CHARSET));
        return new String(bytes, CHARSET);
    }

    public static String decodeBase64(String encodedValue) {
        if(encodedValue == null) {
            return null;
        }
        byte[] bytes = Base64.getUrlDecoder().decode(encodedValue.getBytes(CHARSET));
        return new String(bytes, CHARSET);
    }

}