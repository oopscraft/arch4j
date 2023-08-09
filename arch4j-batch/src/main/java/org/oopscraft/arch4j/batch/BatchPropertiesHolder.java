package org.oopscraft.arch4j.batch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Lazy(false)
@RequiredArgsConstructor
public class BatchPropertiesHolder {

    @Getter
    private static BatchProperties instance;

    private final BatchProperties batchProperties;

    @PostConstruct
    public void postConstruct() {
        instance = batchProperties;
    }

}
