package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("admin/monitor")
@PreAuthorize("hasAuthority('ADMIN_MONITOR')")
@RequiredArgsConstructor
@Slf4j
public class MonitorController {

    private final InfoEndpoint infoEndpoint;

    private final MonitorScheduler monitorScheduler;

    @GetMapping
    public ModelAndView monitor() {
        return new ModelAndView("admin/monitor.html");
    }

    @GetMapping("get-info")
    @ResponseBody
    public Map<String,Object> getInfo() {
        return infoEndpoint.info();
    }

    @GetMapping("get-cpu")
    @ResponseBody
    public List<Map<String,Object>> getCpu() {
        return monitorScheduler.getCpu();
    }

    @GetMapping("get-memory")
    @ResponseBody
    public List<Map<String,Object>> getMemory() {
        return monitorScheduler.getMemory();
    }

    @GetMapping("get-disk")
    @ResponseBody
    public List<Map<String,Object>> getDisk() {
        return monitorScheduler.getDisk();
    }

}
