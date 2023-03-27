package org.oopscraft.arch4j.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class ActiveJobCondition implements ConfigurationCondition {

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.PARSE_CONFIGURATION;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        log.debug("context: {}", context);
        log.debug("metadata: {}", metadata);
        String className = this.parseAnnotatedClassName(metadata);
        String jobNames = context.getEnvironment().getProperty("spring.batch.job.names");
        if(jobNames != null && jobNames.trim().length() > 0){
            return Arrays.asList(jobNames.split(",")).contains(className);
        }
        return false;
    }

    /**
     * find annotated class name
     * @param metadata meta data
     * @return class name
     */
    private String parseAnnotatedClassName(AnnotatedTypeMetadata metadata) {
        String annotatedSource = metadata.getAnnotations().stream()
                .map(a -> Objects.requireNonNull(a.getSource()).toString())
                .distinct()
                .findAny()
                .get();
        if(annotatedSource.startsWith("class ")){
            return annotatedSource.split("\\s+")[1];
        }else{
            return annotatedSource;
        }
    }

}
