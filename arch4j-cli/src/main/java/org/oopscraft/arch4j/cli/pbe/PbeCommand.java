package org.oopscraft.arch4j.cli.pbe;

import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@CommandLine.Command(name = "pbe")
@Component
@RequiredArgsConstructor
public class PbeCommand {

    private final StandardPBEStringEncryptor stringEncryptor;

    @CommandLine.Command(name = "encrypt")
    public Integer encrypt(@CommandLine.Parameters(index="0")String plainValue){
        String encryptedValue = stringEncryptor.encrypt(plainValue);
        System.out.println("plain value:" + plainValue);
        System.out.println("encrypted value:" + encryptedValue);
        return 0;
    }

    @CommandLine.Command(name = "decrypt")
    public Integer decrypt(@CommandLine.Parameters(index="0")String encryptedValue) {
        String plainValue = stringEncryptor.decrypt(encryptedValue);
        System.out.println("encrypted value:" + encryptedValue);
        System.out.println("plain value:" + plainValue);
        return 0;
    }

}
