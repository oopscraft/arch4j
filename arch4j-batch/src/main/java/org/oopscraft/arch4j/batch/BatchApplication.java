package org.oopscraft.arch4j.batch;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.oopscraft.arch4j.core.CoreApplication;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Properties;

@Configuration
@Import(CoreApplication.class)
@ComponentScan(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION, classes=Configuration.class
        )
)
@EnableAutoConfiguration(
        exclude = {
                DataSourceAutoConfiguration.class
        }
)
@EnableConfigurationProperties(BatchProperties.class)
@EnableBatchProcessing
@MapperScan(
        annotationClass = Mapper.class,
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@Slf4j
public class BatchApplication implements EnvironmentPostProcessor {

    public static void main(String[] args) {

        // check args
        if(args == null || args.length < 1) {
            String errorMessage = "Usages: BatchClassName name=value ...";
            System.err.println(errorMessage);
            log.error(errorMessage);
            System.exit(-1);
        }

        // batch configuration
        Class<?> batchClass;
        try {
            batchClass = Class.forName(args[0]);
        }catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        // launch spring boot application
        new SpringApplicationBuilder(BatchApplication.class, batchClass)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.NONE)
                .registerShutdownHook(true)
                .run(args);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Resource resource = new DefaultResourceLoader().getResource("classpath:batch-config.yml");
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource);
        factory.afterPropertiesSet();
        Properties properties = Optional.ofNullable(factory.getObject()).orElseThrow(RuntimeException::new);
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("batch-config", properties);
        environment.getPropertySources().addLast(propertiesPropertySource);
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        HikariDataSource dataSource = (HikariDataSource) dataSourceProperties.initializeDataSourceBuilder()
                .type(dataSourceProperties.getType())
                .build();
        if (StringUtils.hasText(dataSourceProperties.getName())) {
            dataSource.setPoolName(dataSourceProperties.getName());
        }
        return dataSource;
    }

    @Bean
    @BatchDataSource
    public DataSource batchDataSource(BatchProperties batchProperties) {
        return new HikariDataSource(batchProperties.getDatasource());
    }

}
