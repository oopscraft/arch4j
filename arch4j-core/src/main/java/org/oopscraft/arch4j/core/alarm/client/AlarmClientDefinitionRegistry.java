package org.oopscraft.arch4j.core.alarm.client;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AlarmClientDefinitionRegistry implements BeanPostProcessor {

    @Getter
    private static final List<AlarmClientDefinition> alarmClientDefinitions = new ArrayList<>();

    public void add(AlarmClientDefinition alarmClientDefinition) {
        alarmClientDefinitions.add(alarmClientDefinition);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof AlarmClientDefinition) {
            add((AlarmClientDefinition) bean);
        }
        return bean;
    }

    public static Optional<AlarmClientDefinition> getProbeDefinition(String clientType) {
        return alarmClientDefinitions.stream()
                .filter(probe -> Objects.equals(probe.getClientType().getName(), clientType))
                .findFirst();
    }

}
