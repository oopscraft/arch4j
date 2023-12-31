package org.oopscraft.arch4j.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.oopscraft.arch4j.web.security.SecurityPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "web")
@ConstructorBinding
@AllArgsConstructor
@Getter
public final class WebProperties {

    private final String theme = "_default";

    @NotNull
    private final String brand;

    @NotNull
    private final String title;

    @NotNull
    private final String index;

    private final SecurityPolicy securityPolicy = SecurityPolicy.ANONYMOUS;

    @NotNull
    private final String securitySigningKey;

    @NotNull
    private final Integer securityExpireMinutes;

}
