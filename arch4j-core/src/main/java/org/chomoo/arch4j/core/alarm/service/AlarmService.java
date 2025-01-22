package org.chomoo.arch4j.core.alarm.service;

import lombok.RequiredArgsConstructor;
import org.chomoo.arch4j.core.alarm.client.AlarmClient;
import org.chomoo.arch4j.core.alarm.client.AlarmClientFactory;
import org.chomoo.arch4j.core.alarm.dao.AlarmEntity;
import org.chomoo.arch4j.core.alarm.dao.AlarmRepository;
import org.chomoo.arch4j.core.alarm.model.Alarm;
import org.chomoo.arch4j.core.alarm.model.AlarmSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    @Transactional
    public Alarm saveAlarm(Alarm alarm) {
        AlarmEntity alarmEntity = Optional.ofNullable(alarm.getAlarmId())
                .flatMap(alarmRepository::findById)
                .orElse(AlarmEntity.builder()
                        .alarmId(alarm.getAlarmId())
                        .build());

        alarmEntity.setSystemUpdatedAt(LocalDateTime.now());
        alarmEntity.setName(alarm.getName());
        alarmEntity.setAlarmClientId(alarm.getAlarmClientId());
        alarmEntity.setAlarmClientConfig(alarm.getAlarmClientConfig());

        AlarmEntity savedAlarmEntity = alarmRepository.saveAndFlush(alarmEntity);
        return Alarm.from(savedAlarmEntity);
    }

    public Optional<Alarm> getAlarm(String alarmId) {
        return alarmRepository.findById(alarmId)
                .map(Alarm::from);
    }

    @Transactional
    public void deleteAlarm(String alarmId) {
        alarmRepository.deleteById(alarmId);
        alarmRepository.flush();
    }

    public Page<Alarm> getAlarms(AlarmSearch alarmSearch, Pageable pageable) {
        Page<AlarmEntity> page = alarmRepository.findAll(alarmSearch, pageable);
        List<Alarm> alarms = page.getContent().stream()
                .map(Alarm::from)
                .collect(Collectors.toList());
        return new PageImpl<>(alarms, pageable, page.getTotalElements());
    }

    public void sendAlarm(Alarm alarm, String subject, String content) {
        AlarmClient alarmClient = AlarmClientFactory.getAlarmClient(alarm);
        alarmClient.sendMessage(subject, content);
    }

    public void sendAlarm(String alarmId, String subject, String content) {
        Alarm alarm = getAlarm(alarmId).orElseThrow();
        sendAlarm(alarm, subject, content);
    }

}
