package org.oopscraft.arch4j.core.alarm.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.alarm.dao.AlarmEntity;
import org.oopscraft.arch4j.core.common.data.BaseModel;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Alarm extends BaseModel {

    private String alarmId;

    private String name;

    private String alarmClientId;

    private String alarmClientConfig;

    public static Alarm from(AlarmEntity alarmEntity) {
        return Alarm.builder()
                .systemRequired(alarmEntity.isSystemRequired())
                .systemUpdatedAt(alarmEntity.getSystemUpdatedAt())
                .systemUpdatedBy(alarmEntity.getSystemUpdatedBy())
                .alarmId(alarmEntity.getAlarmId())
                .name(alarmEntity.getName())
                .alarmClientId(alarmEntity.getAlarmClientId())
                .alarmClientConfig(alarmEntity.getAlarmClientConfig())
                .build();
    }

}
