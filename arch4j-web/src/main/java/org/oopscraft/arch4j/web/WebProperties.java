package org.oopscraft.arch4j.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties(prefix = "web")
@ConstructorBinding
@AllArgsConstructor
@Getter
public final class WebProperties {

    private final String theme;

    private final List<String> locales;

}
