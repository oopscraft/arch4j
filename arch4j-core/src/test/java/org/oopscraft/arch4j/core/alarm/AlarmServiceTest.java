package org.oopscraft.arch4j.core.alarm;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.alarm.dao.AlarmEntity;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.support.CoreTestSupport;

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
                .alarmName("test")
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
                .alarmName("test")
                .build();
        entityManager.persist(alarmEntity);

        // when
        Alarm alarm = Alarm.builder()
                .alarmId(alarmEntity.getAlarmId())
                .alarmName("changed")
                .build();
        alarmService.saveAlarm(alarm);

        // then
        assertEquals(
                "changed",
                entityManager.find(AlarmEntity.class, alarm.getAlarmId())
                        .getAlarmName()
        );
    }

    @Test
    @Order(3)
    void getAlarm() {
        // given
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .alarmId("test_alarm")
                .alarmName("test alarm")
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
                .alarmName("test alarm")
                .build();
        entityManager.persist(alarmEntity);

        // when
        alarmService.deleteAlarm(alarmEntity.getAlarmId());

        //then
        assertNull(entityManager.find(AlarmEntity.class, alarmEntity.getAlarmId()));
    }

}