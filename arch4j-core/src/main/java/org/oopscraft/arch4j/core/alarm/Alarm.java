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

    private String clientType;

    private String clientProperties;

    private String subject;

    private String content;

    public static Alarm from(AlarmEntity alarmEntity) {
        return Alarm.builder()
                .alarmId(alarmEntity.getAlarmId())
                .alarmName(alarmEntity.getAlarmName())
                .enabled(alarmEntity.isEnabled())
                .clientType(alarmEntity.getClientType())
                .clientProperties(alarmEntity.getClientProperties())
                .subject(alarmEntity.getSubject())
                .content(alarmEntity.getContent())
                .build();
    }

}
