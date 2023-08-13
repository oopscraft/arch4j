package org.oopscraft.arch4j.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.oopscraft.arch4j.core.security.SecurityPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties(prefix = "web")
@ConstructorBinding
@AllArgsConstructor
@Getter
public final class WebProperties {

    private final String theme;

    private final String brand;

    private final String title;

    private final String index;

    private final SecurityPolicy securityPolicy;

}
