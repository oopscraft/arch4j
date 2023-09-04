package org.oopscraft.arch4j.core.alarm.client.slack;

import org.oopscraft.arch4j.core.alarm.client.AlarmClient;
import org.oopscraft.arch4j.core.alarm.client.AlarmClientDefinition;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SlackAlarmClientDefinition implements AlarmClientDefinition {

    @Override
    public Class<? extends AlarmClient> getClientType() {
        return SlackAlarmClient.class;
    }

    @Override
    public String getClientName() {
        return "slack";
    }

    @Override
    public Properties getClientPropertiesTemplate() {
        Properties properties = new Properties();
        properties.setProperty("url", "url");
        properties.setProperty("insecure", "true|false (default is false)");
        return properties;
    }

}
