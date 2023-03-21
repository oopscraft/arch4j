package org.oopscraft.arch4j.web;


import org.oopscraft.arch4j.core.CoreConfiguration;
import org.oopscraft.arch4j.web.security.AuthenticationActionHandler;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/**
 * WebConfiguration
 */
@Configuration
@Import(CoreConfiguration.class)
@ComponentScan(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableAutoConfiguration
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
     * @return
     * @throws Exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**").authorizeRequests()
                // role
                .anyRequest().hasRole("API")
                // login
                .and()
                .httpBasic();
        return http.build();
    }

    /**
     * H2 console security filter chain
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(2)
    public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {

        http.antMatcher("/h2-console/**").authorizeRequests()

                // role
                .anyRequest().permitAll()

                // csrf
                .and()
                .csrf().disable()

                // cors
                .headers().frameOptions().sameOrigin();

        // returns
        return http.build();
    }

    /**
     * Web security filter chain
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(99)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {

        // defines chain
        http.antMatcher("/**/*").authorizeRequests()

                // admin
                .antMatchers("/admin/**")
                .hasRole("ADMIN")

                // csrf
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                // cors
                .and()
                .cors().disable()

                // login
                .formLogin()
                .loginPage("/security/login")
                .loginProcessingUrl("/security/login-process")
                .defaultSuccessUrl("/")
                .failureUrl("/security/login?error=true")
                .permitAll()

                // logout
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/security/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .permitAll();

        // returns
        return http.build();
    }

}
