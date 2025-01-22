package org.chomookun.arch4j.core;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class CorePropertiesHolder {

    @Getter
    private static CoreProperties instance;

    private final CoreProperties coreProperties;

    @PostConstruct
    public void postConstruct() {
        instance = coreProperties;
    }

}
