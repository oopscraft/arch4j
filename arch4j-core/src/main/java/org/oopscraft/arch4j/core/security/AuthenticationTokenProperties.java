package org.oopscraft.arch4j.core.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix="core.security.authentication-token")
@ConstructorBinding
@AllArgsConstructor
@Getter
@Validated
public class AuthenticationTokenProperties {

    @NotNull
    private final String signingKey;

    @NotNull
    private final Integer expireMinutes;

}
