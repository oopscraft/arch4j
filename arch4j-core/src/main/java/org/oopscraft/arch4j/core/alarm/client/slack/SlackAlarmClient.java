package org.oopscraft.arch4j.core.alarm.client.slack;

import org.oopscraft.arch4j.core.alarm.client.AlarmClient;

import java.util.Properties;

public class SlackAlarmClient extends AlarmClient {

    public SlackAlarmClient(Properties properties) {
        super(properties);
    }

    @Override
    public void sendMessage(String message) {

    }

}
