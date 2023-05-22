package org.oopscraft.arch4j.cli;

import org.oopscraft.arch4j.core.CoreApplication;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.util.Optional;
import java.util.Properties;

/**
 * BatchApplication
 */
@Configuration
@Import(CoreApplication.class)
@ComponentScan(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableAutoConfiguration
public class CliApplication implements EnvironmentPostProcessor {

    /**
     * main
     * @param args main arguments
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(CliApplication.class)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.NONE)
                .registerShutdownHook(true)
                .run(args);
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

}
