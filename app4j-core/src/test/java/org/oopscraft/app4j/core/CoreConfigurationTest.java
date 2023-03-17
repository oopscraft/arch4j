package org.oopscraft.app4j.core;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

public class CoreConfigurationTest {

    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(CoreConfiguration.class)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.NONE)
                .registerShutdownHook(true)
                .run(args);
    }

}
