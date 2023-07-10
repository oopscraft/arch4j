package org.oopscraft.arch4j.cli.install;

import org.apache.commons.lang3.ArrayUtils;
import org.oopscraft.arch4j.cli.utils.InteractiveUtils;
import org.oopscraft.arch4j.core.CoreApplication;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class InstallCommand implements Callable<Integer> {

    private final ApplicationArguments applicationArguments;

    public InstallCommand(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    @Override
    public Integer call() throws Exception {
        // create schema
        String driverClassName = InteractiveUtils.askInput("Driver Class Name");
        String jdbcUrl = InteractiveUtils.askInput("Jdbc Url");
        String username = InteractiveUtils.askInput("Username");
        String password = InteractiveUtils.askInput("Password");
        InteractiveUtils.askConfirm("Continue to create schema?");
        createSchema(driverClassName, jdbcUrl, username, password);

        // safety equipment
        System.exit(0);
        return 0;
    }

    public void createSchema(String driverClassName, String jdbcUrl, String username, String password) {
        // origin args
        String[] args = Arrays.copyOf(applicationArguments.getSourceArgs(), applicationArguments.getSourceArgs().length);

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
