package org.oopscraft.arch4j.core.alarm.client.slack;

import org.oopscraft.arch4j.core.alarm.client.AlarmClient;
import org.oopscraft.arch4j.core.alarm.client.AlarmClientDefinition;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.StringJoiner;

@Component
public class SlackAlarmClientDefinition implements AlarmClientDefinition {

    @Override
    public String getId() {
        return "SLACK";
    }

    @Override
    public String getName() {
        return "Slack";
    }

    @Override
    public Class<? extends AlarmClient> getType() {
        return SlackAlarmClient.class;
    }

    @Override
    public String getConfigTemplate() {
        StringJoiner template = new StringJoiner("\n");
        template.add("url=url");
        template.add("insecure=true|false (default is false)");
        return template.toString();
    }

}
