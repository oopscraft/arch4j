package org.oopscraft.arch4j.core.alarm.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "core_alarm")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AlarmEntity extends SystemEntity {

    @Id
    @Column(name = "alarm_id", length = 32)
    private String alarmId;

    @Column(name = "alarm_name")
    private String alarmName;

    @Column(name = "client_type")
    private String clientType;

    @Column(name = "client_config")
    private String clientConfig;

}
