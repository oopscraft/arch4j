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

    public synchronized static CryptoUtil getInstance() {
        synchronized (CryptoUtil.class) {
            if (instance == null) {
                throw new RuntimeException("CryptoUtil not initialized");
            }
            return instance;
        }
    }

    public synchronized static CryptoUtil getInstance(CryptoKeyProvider cryptoKeyProvider) {
        synchronized (CryptoUtil.class) {
            if(instance == null) {
                instance = new CryptoUtil(cryptoKeyProvider);
            }
            return instance;
        }
    }

    @Autowired
    private CryptoUtil(CryptoKeyProvider cryptoKeyProvider) {
        encryptor = Encryptors.queryableText(cryptoKeyProvider.getPassword(), cryptoKeyProvider.getSalt());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CryptoKeyProvider cryptoKeyProvider = applicationContext.getBean(CryptoKeyProvider.class);
        instance = CryptoUtil.getInstance(cryptoKeyProvider);
    }

    public String encrypt(String rawText) {
        if(rawText == null) {
            return null;
        }
        return encryptor.encrypt(rawText);
    }

    public String decrypt(String cipherText) {
        if(cipherText == null) {
            return null;
        }
        return encryptor.decrypt(cipherText);
    }

    public void encryptObject(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            Annotation annotation = field.getAnnotation(Crypto.class);
            if(annotation != null) {
                try {
                    field.setAccessible(true);
                    String rawText = (String) field.get(object);
                    String cipherText = encrypt(rawText);
                    field.set(object, cipherText);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void decryptObject(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            Annotation annotation = field.getAnnotation(Crypto.class);
            if(annotation != null) {
                try {
                    field.setAccessible(true);
                    String cipherText = (String) field.get(object);
                    String rawText = decrypt(cipherText);
                    field.set(object, rawText);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
