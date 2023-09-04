package org.oopscraft.arch4j.core.alarm.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "soma_alarm")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AlarmEntity extends SystemFieldEntity {

    @Id
    @Column(name = "alarm_id", length = 32)
    private String alarmId;

    @Column(name = "alarm_name")
    private String alarmName;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "client_type")
    private String clientType;

    @Column(name = "client_properties")
    private String clientProperties;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

}
