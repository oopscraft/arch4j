package org.chomookun.arch4j.core.alarm.client;

import lombok.Getter;

import java.util.Properties;

public abstract class AlarmClient {

    @Getter
    private final Properties properties;

    public AlarmClient(Properties properties) {
        this.properties = properties;
    }

    public abstract void sendMessage(String subject, String content);

}
