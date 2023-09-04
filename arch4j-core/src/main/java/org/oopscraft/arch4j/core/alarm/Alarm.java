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

    private boolean enabled;

    private String className;

    private String properties;

    public static Alarm from(AlarmEntity alarmEntity) {
        return Alarm.builder()
                .alarmId(alarmEntity.getAlarmId())
                .alarmName(alarmEntity.getAlarmName())
                .enabled(alarmEntity.isEnabled())
                .className(alarmEntity.getClassName())
                .properties(alarmEntity.getProperties())
                .build();
    }

}
