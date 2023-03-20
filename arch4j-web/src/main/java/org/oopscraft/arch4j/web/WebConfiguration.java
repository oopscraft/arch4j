package org.oopscraft.arch4j.web;


import org.oopscraft.arch4j.core.CoreConfiguration;
import org.oopscraft.arch4j.web.security.AuthenticationHandler;
import org.oopscraft.arch4j.web.security.AccessTokenAuthenticationFilter;
import org.oopscraft.arch4j.web.security.AccessTokenService;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.Properties;

/**
 * WebConfiguration
 */
@Configuration
@Import(CoreConfiguration.class)
@ComponentScan(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableAutoConfiguration(
//        exclude = {
//                SecurityAutoConfiguration.class,
//                ManagementWebSecurityAutoConfiguration.class
//        }
)
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebConfiguration implements EnvironmentPostProcessor {

    /**
     * postProcessEnvironment
     * @param environment
     * @param application
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Resource resource = new DefaultResourceLoader().getResource("classpath:web-config.yml");
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource);
        factory.afterPropertiesSet();
        Properties properties = factory.getObject();
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("web-config", properties);
        environment.getPropertySources().addLast(propertiesPropertySource);
    }

    /**
     * API security filter chain
     * @param http
     * @param accessTokenService
     * @return
     * @throws Exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, AccessTokenService accessTokenService) throws Exception {
        http.antMatcher("/api/**").authorizeRequests()
                // rule
                .anyRequest().hasRole("API")
                // login
                .and()
                .httpBasic();
        return http.build();
    }

    /**
     * Web security filter chain
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http, AccessTokenService accessTokenService) throws Exception {

        // authentication handler
        AuthenticationHandler authenticationHandler = new AuthenticationHandler(AuthenticationHandler.ResponseType.VIEW, accessTokenService);

        // csrf
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

        // security chain
        http.antMatcher("/**/*").authorizeRequests()

                // admin
                .antMatchers("/admin/**")
                .hasRole("ADMIN")

                // session policy
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // csrf
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                // filter
                .and()
                .addFilterBefore(new AccessTokenAuthenticationFilter(accessTokenService), UsernamePasswordAuthenticationFilter.class)

                // exception
                .exceptionHandling()
//                .authenticationEntryPoint(authenticationHandler)
//                .accessDeniedHandler(authenticationHandler)

                // login
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/process")
                .successHandler(authenticationHandler)
                .failureHandler(authenticationHandler)
                .permitAll()

                // logout
                .and()
                .logout()
                .logoutSuccessHandler(authenticationHandler)
                .permitAll();

        // returns
        return http.build();
    }

}
