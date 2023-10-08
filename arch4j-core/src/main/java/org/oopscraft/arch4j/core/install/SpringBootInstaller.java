package org.oopscraft.arch4j.core.install;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

import java.lang.reflect.Method;

public class SpringBootInstaller {

    public static void install(Class<?> applicationClass, String[] args) {
        // create schema
        String driverClassName = InteractiveUtils.askInput("Driver Class Name");
        String jdbcUrl = InteractiveUtils.askInput("Jdbc Url");
        String username = InteractiveUtils.askInput("Username");
        String password = InteractiveUtils.askInput("Password");
        InteractiveUtils.askConfirm("Continue to create schema?");

        // data source properties
        args = ArrayUtils.add(args, String.format("--spring.datasource.hikari.driver-class-name=%s", driverClassName));
        args = ArrayUtils.add(args, String.format("--spring.datasource.hikari.jdbc-url=%s", jdbcUrl));
        args = ArrayUtils.add(args, String.format("--spring.datasource.hikari.username=%s", username));
        args = ArrayUtils.add(args, String.format("--spring.datasource.hikari.password=%s", password));

        // sets spring boot properties for initialization
        args = ArrayUtils.add(args, "--logging.level.root=DEBUG");
        args = ArrayUtils.add(args, "--logging.pattern.console=%msg%n");
        args = ArrayUtils.add(args, "--spring.sql.init.mode=always");
        args = ArrayUtils.add(args, "--spring.jpa.hibernate.ddl-auto=create");

        // creates jdbc session table
        args = ArrayUtils.add(args, "--spring.main.web-application-type=servlet");
        args = ArrayUtils.add(args, "--spring.session.store-type=jdbc");
        args = ArrayUtils.add(args, "--spring.session.jdbc.initialize-schema=always");

        // launch spring boot application
        new SpringApplicationBuilder(applicationClass)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .registerShutdownHook(true)
                .run(args);
    }

}
