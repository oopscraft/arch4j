package org.oopscraft.arch4j.cli;

import org.apache.commons.lang3.ArrayUtils;
import org.oopscraft.arch4j.cli.command.DatabaseCommand;
import org.oopscraft.arch4j.cli.command.InstallCommand;
import org.oopscraft.arch4j.core.CoreApplication;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;

/**
 * BatchApplication
 */
@Configuration
@Import(CoreApplication.class)
@ComponentScan(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableAutoConfiguration
@CommandLine.Command(
        subcommands = {
                InstallCommand.class,
                DatabaseCommand.class
        }
)
public class CliApplication implements EnvironmentPostProcessor, ApplicationContextAware, CommandLineRunner {

    private ApplicationContext applicationContext;

    private static int exitCode;

    /**
     * main
     * @param args main arguments
     */
    public static void main(String[] args) {

        // install
        if("install".equals(args[0])) {

            // confirms install
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("Application data is initialized.Would you like to proceed?[Y/n]: ");
                String answer = scanner.nextLine();
                if (!"Y".equals(answer)) {
                    System.out.println("Installation Stopped");
                    System.exit(0);
                }
            }

            // sets spring boot properties for initialization
            args = ArrayUtils.add(args, "--logging.level.root=DEBUG");
            args = ArrayUtils.add(args, "--logging.pattern.console=%msg%n");
            args = ArrayUtils.add(args, "--spring.sql.init.mode=always");
            args = ArrayUtils.add(args, "--spring.jpa.hibernate.ddl-auto=create");

            // creates jdbc session table
            args = ArrayUtils.add(args, "--spring.main.web-application-type=servlet");
            args = ArrayUtils.add(args, "--spring.session.store-type=jdbc");
            args = ArrayUtils.add(args, "--spring.session.jdbc.initialize-schema=always");
        }

        // launch spring boot application
        try {
            new SpringApplicationBuilder(CliApplication.class)
                    .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                    .web(WebApplicationType.NONE)
                    .bannerMode(Banner.Mode.OFF)
                    .registerShutdownHook(true)
                    .run(args);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            System.exit(-1);
        }

        // system exit
        System.exit(exitCode);
    }

    /**
     * postProcessEnvironment
     * @param environment spring environment
     * @param application spring application
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Resource resource = new DefaultResourceLoader().getResource("classpath:cli-config.yml");
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource);
        factory.afterPropertiesSet();
        Properties properties = Optional.ofNullable(factory.getObject()).orElseThrow(RuntimeException::new);
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("cli-config", properties);
        environment.getPropertySources().addLast(propertiesPropertySource);
    }

    /**
     * set application context
     * @param applicationContext application context
     * @throws BeansException beans exception
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * runs command
     * @param args command arguments
     * @throws Exception exception
     */
    @Override
    public void run(String... args) throws Exception {
        CliApplication cliApplication = applicationContext.getBean(CliApplication.class);
        CommandLine.IFactory factory = applicationContext.getBean(CommandLine.IFactory.class);
        CommandLine commandLine = new CommandLine(cliApplication, factory);
        commandLine.setUnmatchedArgumentsAllowed(true);
        exitCode = commandLine.execute(args);
    }

}
