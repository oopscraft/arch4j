package org.chomookun.arch4j.core.alarm.client;

import org.springframework.beans.factory.Aware;

public interface AlarmClientDefinition extends Aware {

    String getAlarmClientId();

    String getAlarmClientName();

    String getAlarmClientConfigTemplate();

    Class<? extends AlarmClient> getClassType();

}
