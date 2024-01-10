package org.oopscraft.arch4j.core.alarm.client;

import org.oopscraft.arch4j.core.alarm.Alarm;
import org.oopscraft.arch4j.core.data.pbe.PbePropertiesUtil;

import java.io.IOException;
import java.io.StringReader;
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
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(config));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid properties string", e);
        }

        properties = PbePropertiesUtil.decode(properties);
        properties = PbePropertiesUtil.unwrapDecryptedMark(properties);

        return properties;
    }

}
