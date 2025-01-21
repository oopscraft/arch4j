package org.oopscraft.arch4j.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class WebPropertiesHolder {

    @Getter
    private static WebProperties instance;

    private final WebProperties webProperties;

    @PostConstruct
    public void postConstruct() {
        instance = webProperties;
    }

}
