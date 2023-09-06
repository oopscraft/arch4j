package org.oopscraft.arch4j.core.alarm;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.alarm.dao.AlarmEntity;
import org.oopscraft.arch4j.core.data.SystemFieldModel;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Alarm extends SystemFieldModel {

    private String alarmId;

    private String alarmName;

    private String clientType;

    private String clientConfig;

    public static Alarm from(AlarmEntity alarmEntity) {
        return Alarm.builder()
                .alarmId(alarmEntity.getAlarmId())
                .alarmName(alarmEntity.getAlarmName())
                .clientType(alarmEntity.getClientType())
                .clientConfig(alarmEntity.getClientConfig())
                .build();
    }

}
