package org.chomoo.arch4j.shell;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ShellApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ShellApplication.class)
                .web(WebApplicationType.NONE)
                .registerShutdownHook(true)
                .run(args);
    }

}
