package org.oopscraft.arch4j.core.alarm.client;

import org.springframework.beans.factory.Aware;

public interface AlarmClientDefinition extends Aware {

    Class<? extends AlarmClient> getClientType();

    String getClientName();

    String getClientConfigTemplate();

}
