package org.oopscraft.arch4j.core.alarm.client;

import org.oopscraft.arch4j.core.alarm.Alarm;
import org.oopscraft.arch4j.core.data.pbe.PbePropertiesUtil;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.Properties;

public class AlarmClientFactory {

    public static AlarmClient getAlarmClient(Alarm alarm) {
        try {
            Class<? extends AlarmClient> clientType = Class.forName(alarm.getClientType()).asSubclass(AlarmClient.class);
            Constructor<? extends AlarmClient> constructor = clientType.getConstructor(Properties.class);
            Properties properties = loadPropertiesFromString(alarm.getClientProperties());
            return constructor.newInstance(properties);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Invalid alarm client type: " + alarm.getClientType(), e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Alarm client constructor not found: " + alarm.getClientType(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties loadPropertiesFromString(String propertiesString) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(propertiesString));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid properties string", e);
        }

        properties = PbePropertiesUtil.decode(properties);
        properties = PbePropertiesUtil.unwrapDecryptedMark(properties);

        return properties;
    }


}
