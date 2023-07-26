package org.oopscraft.arch4j.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class WebApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(WebApplication.class)
                .web(WebApplicationType.SERVLET)
                .registerShutdownHook(true)
                .run(args);
    }

}
