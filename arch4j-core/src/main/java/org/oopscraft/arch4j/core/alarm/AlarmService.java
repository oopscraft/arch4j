package org.oopscraft.arch4j.core.alarm;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.alarm.client.AlarmClient;
import org.oopscraft.arch4j.core.alarm.client.AlarmClientFactory;
import org.oopscraft.arch4j.core.alarm.dao.AlarmEntity;
import org.oopscraft.arch4j.core.alarm.dao.AlarmRepository;
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
        AlarmEntity alarmEntity = Optional.ofNullable(alarm.getId())
                .flatMap(alarmRepository::findById)
                .orElse(AlarmEntity.builder()
                        .id(alarm.getId())
                        .build());

        alarmEntity.setSystemUpdatedAt(LocalDateTime.now());
        alarmEntity.setName(alarm.getName());
        alarmEntity.setAlarmClientId(alarm.getAlarmClientId());
        alarmEntity.setAlarmClientConfig(alarm.getAlarmClientConfig());

        AlarmEntity savedAlarmEntity = alarmRepository.saveAndFlush(alarmEntity);
        return Alarm.from(savedAlarmEntity);
    }

    public Optional<Alarm> getAlarm(String id) {
        return alarmRepository.findById(id)
                .map(Alarm::from);
    }

    @Transactional
    public void deleteAlarm(String id) {
        alarmRepository.deleteById(id);
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

    public void sendAlarm(String id, String subject, String content) {
        Alarm alarm = getAlarm(id).orElseThrow();
        sendAlarm(alarm, subject, content);
    }

}
