package org.oopscraft.arch4j.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.oopscraft.arch4j.core.security.SecurityPolicy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Locale;

@ConfigurationProperties(prefix = "core")
@ConstructorBinding
@AllArgsConstructor
@Getter
public class CoreProperties {

    private final SecurityPolicy securityPolicy = SecurityPolicy.ANONYMOUS;

    private final Locale defaultLocale;

    private final List<Locale> supportedLocales;

}
