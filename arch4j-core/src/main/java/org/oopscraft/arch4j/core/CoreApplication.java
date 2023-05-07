package org.oopscraft.arch4j.core;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.modelmapper.ModelMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.oopscraft.arch4j.core.message.MessageService;
import org.oopscraft.arch4j.core.message.MessageSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.core.SpringProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CoreApplication
 */
@Slf4j
@Configuration
@ComponentScan(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableEncryptableProperties
@EnableJpaRepositories
@EntityScan
@EnableTransactionManagement
@MapperScan(
        annotationClass = Mapper.class,
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class CoreApplication implements EnvironmentPostProcessor {

    /**
     * postProcessEnvironment
     * @param environment environment
     * @param application application
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        // load default core config
        Resource resource = new DefaultResourceLoader().getResource("classpath:core-config.yml");
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource);
        factory.afterPropertiesSet();
        Properties properties = Optional.ofNullable(factory.getObject()).orElseThrow(RuntimeException::new);
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("core-config", properties);
        environment.getPropertySources().addLast(propertiesPropertySource);

        // overrides debug log level
        if("debug".equalsIgnoreCase(environment.getProperty("logging.level.root"))) {
            environment.getSystemProperties().put("logging.level.org.hibernate.SQL", "DEBUG");
            environment.getSystemProperties().put("logging.level.org.hibernate.type.descriptor.sql.BasicBinder", "TRACE");
            environment.getSystemProperties().put("logging.level.jdbc.resultsettable", "DEBUG");
        }
    }

    /**
     * PBE encryption bean
     * @param environment environment
     * @return PBE string encryptor
     */
    @Bean
    public StringEncryptor jasyptEncryptorBean(ConfigurableEnvironment environment) {
        StandardPBEStringEncryptor pbeEncryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig pbeConfig = new EnvironmentStringPBEConfig();
        String password = environment.getProperty("jasypt.encryptor.password");
        pbeConfig.setPassword(password);
        pbeEncryptor.setConfig(pbeConfig);
        return pbeEncryptor;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.messages")
    public MessageSourceProperties messageSourceProperties() {
        return new MessageSourceProperties();
    }

    /**
     * messageSource
     * @return message source
     */
    @Bean
    public MessageSource messageSource(MessageSourceProperties messageProperties, MessageService messageService) {
        MessageSource messageSource = new MessageSource(messageService);
        String basename = messageProperties.getBasename();
        if(basename != null && !basename.isBlank()) {
            String[] basenameArray = StringUtils.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(basename));
            messageSource.setBasenames(Arrays.stream(basenameArray)
                    .map(name -> "classpath*:" + name).toArray(String[]::new));
        }
        if (messageProperties.getEncoding() != null) {
            messageSource.setDefaultEncoding(messageProperties.getEncoding().name());
        }
        messageSource.setFallbackToSystemLocale(messageProperties.isFallbackToSystemLocale());
        Duration cacheDuration = messageProperties.getCacheDuration();
        if (cacheDuration != null) {
            messageSource.setCacheMillis(cacheDuration.toMillis());
        }
        messageSource.setAlwaysUseMessageFormat(messageProperties.isAlwaysUseMessageFormat());
        messageSource.setUseCodeAsDefaultMessage(messageProperties.isUseCodeAsDefaultMessage());
        return messageSource;
    }

    /**
     * JPAQueryFactory for query DSL
     * @param entityManager entity manager
     * @return jpa query factory
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    /**
     * spring security password encoder
     * @return password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
