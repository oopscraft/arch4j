package org.oopscraft.arch4j.core.git.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix="core.git")
@ConstructorBinding
@AllArgsConstructor
@Getter
@Validated
public class GitProperties {

    @NotBlank
    private final String location;

}
