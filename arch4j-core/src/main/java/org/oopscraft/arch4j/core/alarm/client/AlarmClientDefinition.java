package org.oopscraft.arch4j.core.alarm.client;

import org.springframework.beans.factory.Aware;

import java.util.Properties;

public interface AlarmClientDefinition extends Aware {

    Class<? extends AlarmClient> getClientType();

    String getClientName();

    Properties getClientPropertiesTemplate();

}
