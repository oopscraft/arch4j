package org.oopscraft.arch4j.core.alarm.client;

import lombok.Getter;

import java.util.Properties;

public abstract class AlarmClient {

    @Getter
    private final Properties config;

    public AlarmClient(Properties config) {
        this.config = config;
    }

    public abstract void sendMessage(String subject, String content);

}
