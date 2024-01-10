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
                .id(IdGenerator.uuid())
                .name("test")
                .build();

        // when
        Alarm savedAlarm = alarmService.saveAlarm(alarm);

        // then
        assertNotNull(entityManager.find(AlarmEntity.class, savedAlarm.getId()));
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .id(IdGenerator.uuid())
                .name("test")
                .build();
        entityManager.persist(alarmEntity);

        // when
        Alarm alarm = Alarm.builder()
                .id(alarmEntity.getId())
                .name("changed")
                .build();
        alarmService.saveAlarm(alarm);

        // then
        assertEquals(
                "changed",
                entityManager.find(AlarmEntity.class, alarm.getId())
                        .getName()
        );
    }

    @Test
    @Order(3)
    void getAlarm() {
        // given
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .id("test_alarm")
                .name("test alarm")
                .build();
        entityManager.persist(alarmEntity);

        // when
        Alarm alarm = alarmService.getAlarm(alarmEntity.getId()).orElse(null);

        //then
        assertNotNull(alarm);
    }

    @Test
    @Order(4)
    void deleteAlarm() {
        // given
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .id("test_alarm")
                .name("test alarm")
                .build();
        entityManager.persist(alarmEntity);

        // when
        alarmService.deleteAlarm(alarmEntity.getId());

        //then
        assertNull(entityManager.find(AlarmEntity.class, alarmEntity.getId()));
    }

}