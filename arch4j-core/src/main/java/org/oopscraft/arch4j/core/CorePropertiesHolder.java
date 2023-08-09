package org.oopscraft.arch4j.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
