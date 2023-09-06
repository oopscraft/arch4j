package org.oopscraft.arch4j.core.alarm.client.slack;

import org.oopscraft.arch4j.core.alarm.client.AlarmClient;
import org.oopscraft.arch4j.core.alarm.client.AlarmClientDefinition;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.StringJoiner;

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
    public String getClientConfigTemplate() {
        StringJoiner template = new StringJoiner("\n");
        template.add("url=url");
        template.add("insecure=true|false (default is false)");
        return template.toString();
    }

}
