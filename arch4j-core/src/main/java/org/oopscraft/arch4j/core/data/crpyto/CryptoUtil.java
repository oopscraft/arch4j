package org.oopscraft.arch4j.core.data.crpyto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Component
@Lazy(false)
@Slf4j
public class CryptoUtil implements ApplicationContextAware {

    private static CryptoUtil instance;

    private final TextEncryptor encryptor;

    /**
     * return instance
     * @return instance
     */
    public synchronized static CryptoUtil getInstance() {
        synchronized (CryptoUtil.class) {
            if (instance == null) {
                throw new RuntimeException("CryptoUtil not initialized");
            }
            return instance;
        }
    }

    /**
     * return instance
     * @param cryptoKeyProvider crypto key provider
     * @return instance
     */
    public synchronized static CryptoUtil getInstance(CryptoKeyProvider cryptoKeyProvider) {
        synchronized (CryptoUtil.class) {
            if(instance == null) {
                instance = new CryptoUtil(cryptoKeyProvider);
            }
            return instance;
        }
    }

    /**
     * constructor
     * @param cryptoKeyProvider crypto key provider
     */
    @Autowired
    private CryptoUtil(CryptoKeyProvider cryptoKeyProvider) {
        encryptor = Encryptors.delux(cryptoKeyProvider.getPassword(), cryptoKeyProvider.getSalt());
    }

    /**
     * encrypt
     * @param rawText raw text
     * @return cipher text
     */
    public String encrypt(String rawText) {
        if(rawText == null) {
            return null;
        }
        return encryptor.encrypt(rawText);
    }

    /**
     * decrypt
     * @param cipherText cipher text
     * @return raw text
     */
    public String decrypt(String cipherText) {
        if(cipherText == null) {
            return null;
        }
        return encryptor.decrypt(cipherText);
    }

    /**
     * encrypt object field
     * @param object object
     */
    public void encryptObject(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            Annotation annotation = field.getAnnotation(Crypto.class);
            if(annotation != null) {
                String rawText = null;
                try {
                    field.setAccessible(true);
                    rawText = (String) field.get(object);
                    String cipherText = encrypt(rawText);
                    field.set(object, cipherText);
                } catch (Throwable ignore) {
                    log.warn("{} - {}", ignore.getMessage(), rawText);
                }
            }
        }
    }

    /**
     * decrypt object filed
     * @param object object
     */
    public void decryptObject(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            Annotation annotation = field.getAnnotation(Crypto.class);
            if(annotation != null) {
                String cipherText = null;
                try {
                    field.setAccessible(true);
                    cipherText = (String) field.get(object);
                    String rawText = decrypt(cipherText);
                    field.set(object, rawText);
                } catch (Throwable ignore) {
                    log.warn("{} - {}", ignore.getMessage(), cipherText);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CryptoKeyProvider cryptoKeyProvider = applicationContext.getBean(CryptoKeyProvider.class);
        instance = CryptoUtil.getInstance(cryptoKeyProvider);
    }

}
