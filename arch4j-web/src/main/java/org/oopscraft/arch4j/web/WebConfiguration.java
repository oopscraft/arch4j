package org.oopscraft.arch4j.web;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.CoreConfiguration;
import org.oopscraft.arch4j.core.CoreProperties;
import org.oopscraft.arch4j.core.security.service.RoleService;
import org.oopscraft.arch4j.web.security.filter.SecurityFilter;
import org.oopscraft.arch4j.web.security.service.SecurityTokenService;
import org.oopscraft.arch4j.web.security.model.SecurityPolicy;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;

@Configuration
@Import(CoreConfiguration.class)
@ComponentScan(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableAutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
public class WebConfiguration implements EnvironmentPostProcessor, WebMvcConfigurer {

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

    @Bean
    public LocaleResolver localeResolver(CoreProperties coreProperties) {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(coreProperties.getDefaultLocale());
        localeResolver.setCookieName("X-Accept-Language");
        localeResolver.setCookieHttpOnly(true);
        localeResolver.setLanguageTagCompliant(false);
        return localeResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("_language");
        interceptorRegistry.addInterceptor(localeChangeInterceptor)
                .order(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, ZonedDateTime.class, source ->
                ZonedDateTime.parse(source, DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault())));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
                .failOnEmptyBeans(false);
    }

    @Configuration
    @EnableWebSocketMessageBroker
    static class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/ws")
                    .setAllowedOriginPatterns("*");
        }
    }

    @Slf4j
    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
    @RequiredArgsConstructor
    static class SecurityConfiguration {

        private final WebProperties webProperties;

        private final ApplicationContext applicationContext;

        private final AuthenticationEntryPoint authenticationEntryPoint;

        private final AccessDeniedHandler accessDeniedHandler;

        private final AuthenticationSuccessHandler authenticationSuccessHandler;

        private final AuthenticationFailureHandler authenticationFailureHandler;

        private final PlatformTransactionManager transactionManager;

        private final SecurityTokenService authenticationTokenService;

        private final UserDetailsService userDetailsService;

        private final RoleService roleService;

        private SecurityFilter securityFilter() {
            return SecurityFilter.builder()
                    .transactionManager(transactionManager)
                    .authenticationTokenService(authenticationTokenService)
                    .userDetailsService(userDetailsService)
                    .roleService(roleService)
                    .build();
        }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
            return (web) -> web.ignoring()
                    .antMatchers("/static/**");
        }

        @Bean
        protected MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
            DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
            methodSecurityExpressionHandler.setApplicationContext(applicationContext);
            return methodSecurityExpressionHandler;
        }

        @Bean
        protected PersistentTokenRepository persistentTokenRepository(DataSource dataSource, Environment environment) {
            JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
            tokenRepository.setDataSource(dataSource);
            if ("jdbc".equalsIgnoreCase(environment.getProperty("spring.session.store-type"))) {
                try (Connection connection = dataSource.getConnection()) {
                    ResultSet resultSet = connection.getMetaData().getTables(null, null, "persistent_logins", null);
                    if (!resultSet.next()) {
                        tokenRepository.setCreateTableOnStartup(true);
                    }
                } catch (SQLException e) {
                    log.warn(e.getMessage());
                }
            }
            return tokenRepository;
        }

        @Bean
        public RememberMeServices rememberMeServices(PersistentTokenRepository persistentTokenRepository, UserDetailsService userDetailsService) {
            return new PersistentTokenBasedRememberMeServices(webProperties.getSecuritySigningKey(), userDetailsService, persistentTokenRepository);
        }

        @Bean
        @Order(1)
        public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
            http.requestMatcher(request -> new AntPathRequestMatcher("/admin/**").matches(request));
            http.authorizeRequests()
                    .antMatchers("/admin/login**")
                    .permitAll()
                    .anyRequest()
                    .hasAuthority("ADMIN");
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
            http.exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler);
            http.formLogin()
                    .loginPage("/admin/login")
                    .loginProcessingUrl("/admin/login/process")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .permitAll();
            http.logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
                    .logoutSuccessUrl("/admin")
                    .invalidateHttpSession(true)
                    .permitAll();
            // additional security filter
            http.addFilterAfter(securityFilter(), AnonymousAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain ActuatorSecurityFilterChain(HttpSecurity http) throws Exception {
            http.requestMatcher(request -> new AntPathRequestMatcher("/actuator/**").matches(request));
            http.authorizeRequests().anyRequest().hasAuthority("ACTUATOR");
            http.csrf().disable();
            http.headers().frameOptions().sameOrigin();
            // additional security filter
            http.addFilterAfter(securityFilter(), AnonymousAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        @Order(3)
        public SecurityFilterChain swaggerUiSecurityFilterChain(HttpSecurity http) throws Exception {
            http.requestMatcher(request -> new AntPathRequestMatcher("/swagger-ui/**").matches(request));
            http.authorizeRequests().anyRequest().hasAuthority("SWAGGER-UI");
            http.csrf().disable();
            http.headers().frameOptions().sameOrigin();
            http.formLogin()
                    .loginPage("/admin/login")
                    .loginProcessingUrl("/admin/login/process")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .permitAll();
            // additional security filter
            http.addFilterAfter(securityFilter(), AnonymousAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        @Order(4)
        public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
            http.requestMatcher(request -> new AntPathRequestMatcher("/h2-console/**").matches(request));
            http.authorizeRequests().anyRequest().hasAuthority("H2-CONSOLE");
            http.csrf().disable();
            http.headers().frameOptions().sameOrigin();
            http.formLogin()
                    .loginPage("/admin/login")
                    .loginProcessingUrl("/admin/login/process")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .permitAll();
            // additional security filter
            http.addFilterAfter(securityFilter(), AnonymousAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        @Order(98)
        public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
            http.requestMatcher(request -> new AntPathRequestMatcher("/api/**").matches(request));
            if(webProperties.getSecurityPolicy() == SecurityPolicy.ANONYMOUS) {
                http.authorizeRequests()
                        .anyRequest()
                        .permitAll();
            }else{
                http.authorizeRequests()
                        .anyRequest()
                        .authenticated();
            }
            http.csrf().disable();
            http.headers().frameOptions().sameOrigin();

            // remember-me
            http.rememberMe()
                    .rememberMeServices(rememberMeServices)
                    .tokenValiditySeconds(1209600); // default 2 weeks

            // additional security filter
            http.addFilterAfter(securityFilter(), AnonymousAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        @Order(99)
        public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/login**", "/join**")
                    .permitAll();
            http.authorizeRequests()
                    .antMatchers("/user**")
                    .authenticated();
            if(webProperties.getSecurityPolicy() == SecurityPolicy.ANONYMOUS) {
                http.authorizeRequests()
                        .anyRequest()
                        .permitAll();
            }else{
                http.authorizeRequests()
                        .anyRequest()
                        .authenticated();
            }
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
            http.headers().frameOptions().sameOrigin();
            http.exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler);
            http.formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login/process")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .permitAll();
            http.logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .permitAll();

            // remember-me
            http.rememberMe()
                    .rememberMeServices(rememberMeServices)
                    .tokenValiditySeconds(1209600); // default 2 weeks

            // additional security filter
            http.addFilterAfter(securityFilter(), AnonymousAuthenticationFilter.class);
            return http.build();
        }
    }

    @Configuration
    @OpenAPIDefinition(
            info = @Info(title = "REST API"),
            security = { @SecurityRequirement(name = "Authorization") },
            servers = {
                    @Server(url = "/", description = "Default Server URL")
            }
    )
    @SecurityScheme(
            name = "Authorization",
            type = SecuritySchemeType.APIKEY,
            in = SecuritySchemeIn.HEADER
    )
    public static class OpenApiConfiguration {
    }

}
