package org.oopscraft.arch4j.web;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.CoreApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
public class WebApplication implements EnvironmentPostProcessor {

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

    @Slf4j
    @Configuration
    @EnableWebSecurity
    // TODO @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
    static class SecurityConfiguration {

        @Value("${spring.security.anonymous.authorities}")
        private String anonymousAuthorities;

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
                http.anonymous().authorities(anonymousAuthorities);
            }
            // has ADMIN role
            http.authorizeRequests().anyRequest().hasRole("ADMIN");
            // csrf
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
            // cors
            http.cors().disable();
            // login
            http.formLogin()
                    .loginPage("/security/login")
                    .loginProcessingUrl("/security/login-process")
                    .defaultSuccessUrl("/")
                    .failureUrl("/security/login?error=true")
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
            // has ADMIN role
            http.authorizeRequests().anyRequest().hasRole("API");
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
            // has ADMIN role
            http.authorizeRequests().anyRequest().hasRole("ACTUATOR");
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
            // has ADMIN role
            http.authorizeRequests().anyRequest().hasRole("H2-CONSOLE");
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
            // admin
            http.authorizeRequests().anyRequest().permitAll();
            // csrf
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
            // login
            http.formLogin()
                    .loginPage("/security/login")
                    .loginProcessingUrl("/security/login-process")
                    .defaultSuccessUrl("/")
                    .failureUrl("/security/login?error=true")
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
