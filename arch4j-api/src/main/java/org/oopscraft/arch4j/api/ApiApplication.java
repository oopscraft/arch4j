package org.oopscraft.arch4j.api;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

public class ApiApplication {

    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiConfiguration.class)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.SERVLET)
                .registerShutdownHook(true)
                .run(args);
    }

}
