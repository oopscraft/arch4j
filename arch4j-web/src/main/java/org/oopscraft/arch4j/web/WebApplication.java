package org.oopscraft.arch4j.web;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.CoreApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;

/**
 * WebApplication
 */
@Configuration
@Import(CoreApplication.class)
@ComponentScan(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableAutoConfiguration
public class WebApplication implements EnvironmentPostProcessor, WebMvcConfigurer {

    /**
     * main
     * @param args main arguments
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(WebApplication.class)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.SERVLET)
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
        Resource resource = new DefaultResourceLoader().getResource("classpath:web-config.yml");
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource);
        factory.afterPropertiesSet();
        Properties properties = Optional.ofNullable(factory.getObject()).orElseThrow(RuntimeException::new);
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("web-config", properties);
        environment.getPropertySources().addLast(propertiesPropertySource);
    }

    /**
     * creates locale resolver
     * @return locale resolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setCookieName("X-Accept-Language");
        localeResolver.setLanguageTagCompliant(false);
        return localeResolver;
    }

    /**
     * creates interceptor registry
     * @param interceptorRegistry interceptor registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("_language");
        interceptorRegistry.addInterceptor(localeChangeInterceptor);
    }

    /**
     * customizes java.time.* Serializer/Deserializer
     * @return jackson customizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
                .failOnEmptyBeans(false)
                .serializerByType(LocalDateTime.class, new JsonSerializer<LocalDateTime>(){
                    @Override
                    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        String result = value.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        gen.writeString(result);
                    }
                })
                .deserializerByType(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
                        String value = p.getValueAsString();
                        return LocalDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    }
                })
                .serializerByType(LocalDate.class, new JsonSerializer<LocalDate>(){
                    @Override
                    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        String result = value.format(DateTimeFormatter.ISO_DATE);
                        gen.writeString(result);
                    }
                })
                .deserializerByType(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonParser p, DeserializationContext context) throws IOException {
                        String value = p.getValueAsString();
                        return LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
                    }
                });
    }

    /**
     * spring security configuration
     */
    @Slf4j
    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
    @RequiredArgsConstructor
    static class SecurityConfiguration {

        @Value("${spring.security.anonymous.authorities}")
        private String anonymousAuthorities;

        private final AuthenticationSuccessHandler authenticationSuccessHandler;

        private final AuthenticationFailureHandler authenticationFailureHandler;

        /**
         * admin security filter chain
         *
         * @param http
         * @return
         * @throws Exception
         */
        @Bean
        @Order(1)
        public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
            http.requestMatcher(request -> {
                if(new AntPathRequestMatcher("/admin/**").matches(request)){
                    return true;
                }
                return false;
            });
            // anonymous role
            if(anonymousAuthorities != null && !anonymousAuthorities.isBlank()) {
                http.anonymous().authorities(anonymousAuthorities.split(","));
            }
            // has ADMIN role
            http.authorizeRequests().anyRequest().hasAuthority("ADMIN");
            // csrf
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
            // cors
            http.cors().disable();
            // login
            http.formLogin()
                    .loginPage("/security/login")
                    .loginProcessingUrl("/security/login-process")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .permitAll();
            // logout
            http.logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/security/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .permitAll();
            // returns
            return http.build();
        }

        /**
         * API security filter chain
         *
         * @param http http
         * @return security filter chain
         * @throws Exception exception
         */
        @Bean
        @Order(2)
        public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
            http.requestMatcher(request -> {
                if(new AntPathRequestMatcher("/api/**").matches(request)){
                    return true;
                }
                return false;
            });
            // anonymous role
            if(anonymousAuthorities != null && !anonymousAuthorities.isBlank()) {
                http.anonymous().authorities(anonymousAuthorities.split(","));
            }
            // has ADMIN role
            http.authorizeRequests().anyRequest().hasAuthority("API");
            // cors
            http.headers().frameOptions().sameOrigin();
            // returns
            return http.build();
        }

        /**
         * Actuator security filter chain
         *
         * @param http http
         * @return security filter chain
         * @throws Exception exception
         */
        @Bean
        @Order(3)
        public SecurityFilterChain ActuatorSecurityFilterChain(HttpSecurity http) throws Exception {
            http.requestMatcher(request -> {
                if(new AntPathRequestMatcher("/actuator/**").matches(request)){
                    return true;
                }
                return false;
            });
            // anonymous role
            if(anonymousAuthorities != null && !anonymousAuthorities.isBlank()) {
                http.anonymous().authorities(anonymousAuthorities.split(","));
            }
            // has ADMIN role
            http.authorizeRequests().anyRequest().hasAuthority("ACTUATOR");
            // csrf
            http.csrf().disable();
            // cors
            http.headers().frameOptions().sameOrigin();
            // returns
            return http.build();
        }

        /**
         * H2 console security filter chain
         *
         * @param http
         * @return
         * @throws Exception
         */
        @Bean
        @Order(4)
        public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
            http.requestMatcher(request -> {
                if(new AntPathRequestMatcher("/h2-console/**").matches(request)){
                    return true;
                }
                return false;
            });
            // anonymous role
            if(anonymousAuthorities != null && !anonymousAuthorities.isBlank()) {
                http.anonymous().authorities(anonymousAuthorities.split(","));
            }
            // has ADMIN role
            http.authorizeRequests().anyRequest().hasAuthority("H2-CONSOLE");
            // csrf
            http.csrf().disable();
            // cors
            http.headers().frameOptions().sameOrigin();
            // returns
            return http.build();
        }

        /**
         * default security filter chain
         *
         * @param http
         * @return
         * @throws Exception
         */
        @Bean
        @Order(99)
        public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
            http.requestMatcher(request -> {
                return true;
            });
            // anonymous role
            if(anonymousAuthorities != null && !anonymousAuthorities.isBlank()) {
                http.anonymous().authorities(anonymousAuthorities.split(","));
            }
            // admin
            http.authorizeRequests().anyRequest().permitAll();
            // csrf
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
            // login
            http.formLogin()
                    .loginPage("/security/login")
                    .loginProcessingUrl("/security/login-process")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .permitAll();
            // logout
            http.logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/security/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .permitAll();
            // returns
            return http.build();
        }
    }

    /**
     * httpTraceRepository
     * @return httpTraceRepository
     */
    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

    /**
     * OpenApiConfiguration
     */
    @Configuration
    @OpenAPIDefinition(
            info = @Info(title = "REST API"),
            security = { @SecurityRequirement(name = "Authorization") }
    )
    @SecurityScheme(
            name = "Authorization",
            type = SecuritySchemeType.APIKEY,
            in = SecuritySchemeIn.HEADER
    )
    public static class OpenApiConfiguration {

    }

}
