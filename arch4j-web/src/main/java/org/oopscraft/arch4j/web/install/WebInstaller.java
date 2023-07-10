package org.oopscraft.arch4j.web.install;

import org.apache.commons.lang3.ArrayUtils;
import org.oopscraft.arch4j.core.CoreApplication;
import org.oopscraft.arch4j.core.install.CoreInstaller;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

import java.util.Properties;

public class WebInstaller extends CoreInstaller {

    public WebInstaller(String[] args) {
        super(args);
    }

    @Override
    public void install() {

        // create schema
        String driverClassName = askInput("Driver Class Name");
        String jdbcUrl = askInput("Jdbc Url");
        String username = askInput("Username");
        String password = askInput("Password");
        askConfirm("Continue to create schema?");
        createSchema(driverClassName, jdbcUrl, username, password);
    }

    public void createSchema(String driverClassName, String jdbcUrl, String username, String password) {
        String[] args = cloneArgs();

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
        ConfigurableApplicationContext context = new SpringApplicationBuilder(CoreApplication.class)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .registerShutdownHook(true)
                .run(args);
        context.close();
    }

}
