package org.chomookun.arch4j.web;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.CoreConfiguration;
import org.chomookun.arch4j.core.CoreProperties;
import org.chomookun.arch4j.core.security.service.RoleService;
import org.chomookun.arch4j.web.security.filter.SecurityFilter;
import org.chomookun.arch4j.web.security.service.SecurityTokenService;
import org.chomookun.arch4j.web.security.model.SecurityPolicy;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.cache.annotation.EnableCaching;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

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
@EnableScheduling
@EnableCaching
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
        @Override
        public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
            registry.setMessageSizeLimit(2 * 1024 * 1024) // 2MB (default: 64KB)
                    .setSendBufferSizeLimit(4 * 1024 * 1024) // 4MB (default: 512KB)
                    .setTimeToFirstMessage(10_000); // 10 second first message (default: 5s)
        }
    }

    @Slf4j
    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(securedEnabled = true, prePostEnabled = false)
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
                    .requestMatchers("/static/**");
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
        public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
            // security matcher
            MvcRequestMatcher securityMatcher = new MvcRequestMatcher(introspector, "/admin/**");
            http.securityMatcher(securityMatcher);

            // sets authorize
            http.authorizeHttpRequests(authorizeHttpRequests ->
                    authorizeHttpRequests.requestMatchers("/admin/login**")
                            .permitAll()
                            .anyRequest()
                            .hasAuthority("admin")
            );

            // csrf
            CsrfTokenRequestAttributeHandler csrfTokenRequestHandler = new CsrfTokenRequestAttributeHandler();
            csrfTokenRequestHandler.setCsrfRequestAttributeName(null);
            http.csrf(csrf -> {
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                csrf.csrfTokenRequestHandler(csrfTokenRequestHandler);
            });

            // exception
            http.exceptionHandling(exceptionHandling -> {
                exceptionHandling.authenticationEntryPoint(authenticationEntryPoint);
                exceptionHandling.accessDeniedHandler(accessDeniedHandler);
            });

            // login
            http.formLogin(formLogin -> {
                formLogin.loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login/process")
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                        .permitAll();
            });

            // logout
            http.logout(logout -> {
                logout.logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"));
                logout.logoutSuccessUrl("/admin");
                logout.invalidateHttpSession(true);
                logout.permitAll();
            });

            // additional security filter
            http.addFilterAfter(securityFilter(), AnonymousAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain ActuatorSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector, WebEndpointProperties webEndpointProperties) throws Exception {
            // security matcher
            String actuatorBasePath = webEndpointProperties.getBasePath();
            String actuatorMatcherPattern = String.format("%s/**", actuatorBasePath);
            MvcRequestMatcher securityMatcher = new MvcRequestMatcher(introspector, actuatorMatcherPattern);
            http.securityMatcher(securityMatcher);

            // authorize
            http.authorizeHttpRequests(authorizeHttpRequests ->
                    authorizeHttpRequests.anyRequest()
                            .hasAuthority("actuator")
            );

            // csrf
            http.csrf(AbstractHttpConfigurer::disable);

            // headers
            http.headers(headers -> {
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
            });

            // custom filter
            http.addFilterAfter(securityFilter(), AnonymousAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        @Order(3)
        @ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true", matchIfMissing = true)
        public SecurityFilterChain swaggerUiSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector, Environment environment) throws Exception {
            // security matcher
            String swaggerBasePath = environment.getProperty("springdoc.swagger-ui.path", "/swagger-ui");
            String swaggerMatcherPattern = String.format("%s/**", swaggerBasePath);
            MvcRequestMatcher securityMatcher = new MvcRequestMatcher(introspector, swaggerMatcherPattern);
            http.securityMatcher(securityMatcher);

            // authorize
            http.authorizeHttpRequests(authorizeHttpRequests -> {
                authorizeHttpRequests.anyRequest().hasAuthority("swagger-ui");
            });

            // csrf
            http.csrf(AbstractHttpConfigurer::disable);

            // headers
            http.headers(headers ->
                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

            // login
            http.formLogin(formLogin -> {
                formLogin.loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login/process")
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                        .permitAll();
            });

            // custom filter
            http.addFilterAfter(securityFilter(), AnonymousAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        @Order(4)
        @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
        public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
            // security matcher
            http.securityMatcher(PathRequest.toH2Console());

            // authorize
            http.authorizeHttpRequests(authorizeHttpRequests -> {
                authorizeHttpRequests.anyRequest().hasAuthority("h2-console");
            });

            // csrf
            http.csrf(AbstractHttpConfigurer::disable);

            // headers
            http.headers(headers ->
                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

            // login
            http.formLogin(formLogin -> {
                formLogin.loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login/process")
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                        .permitAll();
            });

            // custom filter
            http.addFilterAfter(securityFilter(), AnonymousAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        @Order(98)
        public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http,  HandlerMappingIntrospector introspector, RememberMeServices rememberMeServices) throws Exception {
            // security matcher
            MvcRequestMatcher securityMatcher = new MvcRequestMatcher(introspector, "/api/**");
            http.securityMatcher(securityMatcher);

            // authorize
            http.authorizeHttpRequests(authorizeHttpRequests -> {
                authorizeHttpRequests.requestMatchers("/api/*/login**", "/api/*/join**").permitAll();
                if(webProperties.getSecurityPolicy() == SecurityPolicy.ANONYMOUS) {
                    authorizeHttpRequests.anyRequest().permitAll();
                }else{
                    authorizeHttpRequests.anyRequest().authenticated();
                }
            });

            // csrf
            http.csrf(AbstractHttpConfigurer::disable);

            // headers
            http.headers(headers -> {
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
            });

            // session
            http.sessionManagement(sessionManagement -> {
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
            });

            // exception handling
            http.exceptionHandling(exceptionHandling -> {
                exceptionHandling.accessDeniedHandler(accessDeniedHandler);
            });

            // remember-me
            http.rememberMe(rememberMe -> {
                rememberMe.rememberMeServices(rememberMeServices);
                rememberMe.tokenValiditySeconds(1209600);
            });

            // custom filter
            http.addFilterAfter(securityFilter(), AnonymousAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        @Order(99)
        public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector, RememberMeServices rememberMeServices) throws Exception {
            // security matcher
            MvcRequestMatcher securityMatcher = new MvcRequestMatcher(introspector, "/**");
            http.securityMatcher(securityMatcher);

            // authorize http requests
            http.authorizeHttpRequests(authorizeHttpRequests -> {
                authorizeHttpRequests.requestMatchers("/login**", "/join**").permitAll();
                authorizeHttpRequests.requestMatchers("/user**").authenticated();
                if(webProperties.getSecurityPolicy() == SecurityPolicy.ANONYMOUS) {
                    authorizeHttpRequests.anyRequest().permitAll();
                }else{
                    authorizeHttpRequests.anyRequest().authenticated();
                }
            });

            // csrf
            CsrfTokenRequestAttributeHandler csrfTokenRequestHandler = new CsrfTokenRequestAttributeHandler();
            csrfTokenRequestHandler.setCsrfRequestAttributeName(null);
            http.csrf(csrf -> {
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                csrf.csrfTokenRequestHandler(csrfTokenRequestHandler);
            });

            // headers
            http.headers(headers -> {
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
            });

            // session
            http.sessionManagement(it -> {
                    it.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
            });

            // exception handling
            http.exceptionHandling(exceptonHandling -> {
                exceptonHandling.authenticationEntryPoint(authenticationEntryPoint);
                exceptonHandling.accessDeniedHandler(accessDeniedHandler);
            });

            // login
            http.formLogin(formLogin -> {
                formLogin.loginPage("/login")
                        .loginProcessingUrl("/login/process")
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                        .permitAll();
            });

            // logout
            http.logout(logout -> {
                logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
                logout.logoutSuccessUrl("/");
                logout.invalidateHttpSession(true);
                logout.permitAll();
            });

            // remember me
            http.rememberMe(rememberMe -> {
                rememberMe.rememberMeServices(rememberMeServices);
                rememberMe.tokenValiditySeconds(1209600); // default 2 weeks
            });

            // custom filter
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
