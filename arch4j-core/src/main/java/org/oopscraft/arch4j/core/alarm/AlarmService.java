package org.oopscraft.arch4j.core.alarm;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.alarm.dao.AlarmEntity;
import org.oopscraft.arch4j.core.alarm.dao.AlarmRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        alarmEntity.setAlarmName(alarm.getAlarmName());
        alarmEntity.setEnabled(alarm.isEnabled());
        alarmEntity.setClientType(alarm.getClientType());
        alarmEntity.setClientProperties(alarm.getClientProperties());
        alarmEntity.setSubject(alarm.getSubject());
        alarmEntity.setContent(alarm.getContent());

        alarmEntity = alarmRepository.saveAndFlush(alarmEntity);

        return Alarm.from(alarmEntity);
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

}
