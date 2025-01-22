package org.oopscraft.arch4j.cli;

import org.apache.commons.lang3.ArrayUtils;
import org.oopscraft.arch4j.cli.common.InteractiveUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import picocli.CommandLine;

public class CliApplication implements ApplicationContextAware, CommandLineRunner {

    private static ApplicationContext applicationContext;

    private static int exitCode;

    public static void main(String[] args) {

        // install
        if("install".equals(args[0])) {
            args = addInstallationArguments(args);
        }

        // launch spring boot application
        try {
            new SpringApplicationBuilder(CliApplication.class, CliConfiguration.class)
                    .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                    .web(WebApplicationType.NONE)
                    .bannerMode(Banner.Mode.OFF)
                    .registerShutdownHook(true)
                    .run(args);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            System.exit(-1);
        }
        System.exit(exitCode);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContextInstance) throws BeansException {
        applicationContext = applicationContextInstance;
    }

    @Override
    public void run(String... args) throws Exception {
        exitCode = runCommand(applicationContext, CliConfiguration.class, args);
    }

    public static String[] addInstallationArguments(String[] args) {
        if(!args[0].equals("install")) {
            return args;
        }

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

        // return
        return args;
    }

    public static int runCommand(ApplicationContext applicationContext, Class<?> configurationClass, String[] args) {
        Object configurationObject = applicationContext.getBean(configurationClass);
        CommandLine.IFactory factory = applicationContext.getBean(CommandLine.IFactory.class);
        CommandLine commandLine = new CommandLine(configurationObject, factory);
        commandLine.setUnmatchedArgumentsAllowed(true);
        return commandLine.execute(args);
    }


}
