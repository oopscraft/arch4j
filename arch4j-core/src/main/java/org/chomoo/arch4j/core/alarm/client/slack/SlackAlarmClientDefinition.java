package org.chomoo.arch4j.core.alarm.client.slack;

import org.chomoo.arch4j.core.alarm.client.AlarmClient;
import org.chomoo.arch4j.core.alarm.client.AlarmClientDefinition;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
public class SlackAlarmClientDefinition implements AlarmClientDefinition {

    @Override
    public String getAlarmClientId() {
        return "SLACK";
    }

    @Override
    public String getAlarmClientName() {
        return "Slack";
    }

    @Override
    public String getAlarmClientConfigTemplate() {
        StringJoiner template = new StringJoiner("\n");
        template.add("url=url");
        template.add("insecure=true|false (default is false)");
        return template.toString();
    }

    @Override
    public Class<? extends AlarmClient> getClassType() {
        return SlackAlarmClient.class;
    }

}
