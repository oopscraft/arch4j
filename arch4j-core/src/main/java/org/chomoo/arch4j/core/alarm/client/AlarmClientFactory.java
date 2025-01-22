package org.chomoo.arch4j.core.alarm.client;

import org.chomoo.arch4j.core.alarm.model.Alarm;
import org.chomoo.arch4j.core.common.pbe.PbePropertiesUtil;

import java.lang.reflect.Constructor;
import java.util.Properties;

public class AlarmClientFactory {

    public static AlarmClient getAlarmClient(Alarm alarm) {
        AlarmClientDefinition alarmClientDefinition = AlarmClientDefinitionRegistry.getAlarmClientDefinition(alarm.getAlarmClientId()).orElseThrow();
        try {
            Class<? extends AlarmClient> clientType = alarmClientDefinition.getClassType().asSubclass(AlarmClient.class);
            Constructor<? extends AlarmClient> constructor = clientType.getConstructor(Properties.class);
            Properties config = loadPropertiesFromString(alarm.getAlarmClientConfig());
            return constructor.newInstance(config);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Alarm client constructor not found: " + alarm.getAlarmClientId(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties loadPropertiesFromString(String config) {
        return PbePropertiesUtil.loadProperties(config);
    }

}
