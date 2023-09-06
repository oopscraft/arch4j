package org.oopscraft.arch4j.core.alarm.client;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlarmClientDefinitionRegistry implements BeanPostProcessor {

    @Getter
    private final List<AlarmClientDefinition> alarmClientDefinitions = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if(bean instanceof AlarmClientDefinition) {
            alarmClientDefinitions.add((AlarmClientDefinition) bean);
        }
        return bean;
    }

}
