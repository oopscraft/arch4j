package org.oopscraft.arch4j.cli;

import org.oopscraft.arch4j.cli.database.DatabaseCommand;
import org.oopscraft.arch4j.cli.pbe.PbeCommand;
import org.oopscraft.arch4j.cli.install.InstallCommand;
import org.oopscraft.arch4j.core.CoreApplication;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.*;
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

import java.util.Optional;
import java.util.Properties;

@Configuration
@Import(CoreApplication.class)
@ComponentScan(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableAutoConfiguration
@CommandLine.Command(
        subcommands = {
                PbeCommand.class,
                DatabaseCommand.class
        }
)
public class CliApplication implements EnvironmentPostProcessor, ApplicationContextAware, CommandLineRunner {

    private static ApplicationArguments applicationArguments;

    private static ApplicationContext applicationContext;

    private static int exitCode;

    public static void main(String[] args) throws Exception {

        // install command
        applicationArguments = new DefaultApplicationArguments(args);
        if(applicationArguments.getNonOptionArgs().contains("install")) {
            new InstallCommand(applicationArguments).call();
            System.exit(0);
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContextInstance) throws BeansException {
        applicationContext = applicationContextInstance;
    }

    @Override
    public void run(String... args) throws Exception {
        CliApplication cliApplication = applicationContext.getBean(CliApplication.class);
        CommandLine.IFactory factory = applicationContext.getBean(CommandLine.IFactory.class);
        CommandLine commandLine = new CommandLine(cliApplication, factory);
        commandLine.setUnmatchedArgumentsAllowed(true);
        exitCode = commandLine.execute(args);
    }

}
