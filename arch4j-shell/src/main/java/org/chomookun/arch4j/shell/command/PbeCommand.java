package org.chomookun.arch4j.shell.command;

import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class PbeCommand {

    private final StandardPBEStringEncryptor stringEncryptor;

    @ShellMethod(key = "pbe-encrypt", value = "Encrypts the given text.")
    public int encrypt(String plainValue) {
        String encryptedValue = stringEncryptor.encrypt(plainValue);
        System.out.println("plain value:" + plainValue);
        System.out.println("encrypted value:" + encryptedValue);
        return 0;
    }

    @ShellMethod(key = "pbe-decrypt", value = "Decrypts the given text.")
    public int decrypt(String encryptedValue) {
        String plainValue = stringEncryptor.decrypt(encryptedValue);
        System.out.println("encrypted value:" + encryptedValue);
        System.out.println("plain value:" + plainValue);
        return 0;
    }

}
