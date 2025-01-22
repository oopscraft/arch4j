package org.chomookun.arch4j.core.alarm.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.alarm.dao.AlarmEntity;
import org.chomookun.arch4j.core.alarm.model.Alarm;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class AlarmServiceTest extends CoreTestSupport {

    private final AlarmService alarmService;

    @Test
    @Order(1)
    void saveToPersist() {
        // given
        Alarm alarm = Alarm.builder()
                .alarmId(IdGenerator.uuid())
                .name("test")
                .build();

        // when
        Alarm savedAlarm = alarmService.saveAlarm(alarm);

        // then
        assertNotNull(entityManager.find(AlarmEntity.class, savedAlarm.getAlarmId()));
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .alarmId(IdGenerator.uuid())
                .name("test")
                .build();
        entityManager.persist(alarmEntity);

        // when
        Alarm alarm = Alarm.builder()
                .alarmId(alarmEntity.getAlarmId())
                .name("changed")
                .build();
        alarmService.saveAlarm(alarm);

        // then
        assertEquals(
                "changed",
                entityManager.find(AlarmEntity.class, alarm.getAlarmId())
                        .getName()
        );
    }

    @Test
    @Order(3)
    void getAlarm() {
        // given
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .alarmId("test_alarm")
                .name("test alarm")
                .build();
        entityManager.persist(alarmEntity);

        // when
        Alarm alarm = alarmService.getAlarm(alarmEntity.getAlarmId()).orElse(null);

        //then
        assertNotNull(alarm);
    }

    @Test
    @Order(4)
    void deleteAlarm() {
        // given
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .alarmId("test_alarm")
                .name("test alarm")
                .build();
        entityManager.persist(alarmEntity);

        // when
        alarmService.deleteAlarm(alarmEntity.getAlarmId());

        //then
        assertNull(entityManager.find(AlarmEntity.class, alarmEntity.getAlarmId()));
    }

}