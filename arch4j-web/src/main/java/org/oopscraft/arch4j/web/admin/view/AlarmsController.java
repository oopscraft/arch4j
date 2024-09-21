package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.alarm.model.Alarm;
import org.oopscraft.arch4j.core.alarm.model.AlarmSearch;
import org.oopscraft.arch4j.core.alarm.service.AlarmService;
import org.oopscraft.arch4j.core.alarm.client.AlarmClientDefinitionRegistry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/alarms")
@PreAuthorize("hasAuthority('admin.alarms')")
@RequiredArgsConstructor
public class AlarmsController {

    private final AlarmService alarmService;

    @GetMapping
    public ModelAndView alarms() {
        ModelAndView modelAndView = new ModelAndView("admin/alarms.html");
        modelAndView.addObject("alarmClientDefinitions", AlarmClientDefinitionRegistry.getAlarmClientDefinitions());
        return modelAndView;
    }

    @GetMapping("get-alarms")
    @ResponseBody
    public Page<Alarm> getAlarms(AlarmSearch alarmSearch, Pageable pageable) {
        return alarmService.getAlarms(alarmSearch, pageable);
    }

    @GetMapping("get-alarm")
    @ResponseBody
    public Alarm getAlarm(@RequestParam("alarmId")String alarmId) {
        return alarmService.getAlarm(alarmId)
                .orElseThrow();
    }

    @PostMapping("save-alarm")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.alarms.edit')")
    public Alarm saveAlarm(@RequestBody @Valid Alarm alarm) {
        return alarmService.saveAlarm(alarm);
    }

    @GetMapping("delete-alarm")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.alarms.edit')")
    public void deleteAlarm(@RequestParam("alarmId")String alarmId) {
        alarmService.deleteAlarm(alarmId);
    }

    @PostMapping("test-alarm")
    @ResponseBody
    public void testAlarm(@RequestBody Alarm alarm) {
        alarmService.sendAlarm(alarm,"Test Subject", "Test Content");
    }

}
