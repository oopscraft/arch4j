package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.alarm.Alarm;
import org.oopscraft.arch4j.core.alarm.AlarmSearch;
import org.oopscraft.arch4j.core.alarm.AlarmService;
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
@RequestMapping("admin/alarm")
@PreAuthorize("hasAuthority('ADMIN_ALARM')")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    private final AlarmClientDefinitionRegistry alarmClientDefinitionRegistry;

    @GetMapping
    public ModelAndView variable() {
        ModelAndView modelAndView = new ModelAndView("admin/alarm.html");
        modelAndView.addObject("alarmClientDefinitions", alarmClientDefinitionRegistry.getAlarmClientDefinitions());
        return modelAndView;
    }

    @GetMapping("get-alarms")
    @ResponseBody
    public Page<Alarm> getVariables(AlarmSearch alarmSearch, Pageable pageable) {
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
    @PreAuthorize("hasAuthority('ADMIN_ALARM_EDIT')")
    public Alarm saveAlarm(@RequestBody @Valid Alarm alarm) {
        return alarmService.saveAlarm(alarm);
    }

    @GetMapping("delete-alarm")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN_ALARM_EDIT')")
    public void deleteAlarm(@RequestParam("alarmId")String alarmId) {
        alarmService.deleteAlarm(alarmId);
    }

    @PostMapping("test-alarm")
    @ResponseBody
    public void testAlarm(@RequestBody Alarm alarm) {
        alarmService.sendAlarm(alarm,"Test", "Test");
    }

}
