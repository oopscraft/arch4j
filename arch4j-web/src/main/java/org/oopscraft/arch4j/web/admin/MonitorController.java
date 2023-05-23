package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.info.JavaInfo;
import org.springframework.boot.info.OsInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@RequestMapping("admin/monitor")
@RequiredArgsConstructor
@Slf4j
public class MonitorController {

    private final OsInfo osInfo = new OsInfo();

    private final JavaInfo javaInfo = new JavaInfo();

    private final InfoEndpoint infoEndpoint;

    private final MetricsEndpoint metricsEndpoint;

    private List<Map<String,Object>> cpu = new CopyOnWriteArrayList<>();

    private List<Map<String,Object>> memory = new CopyOnWriteArrayList<>();

    private List<Map<String,Object>> disk = new CopyOnWriteArrayList<>();

    /**
     * monitor page
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/monitor.html");
    }

    /**
     * returns info
     * @return info
     */
    @GetMapping("get-info")
    @ResponseBody
    public Map<String,Object> getInfo() {
        return infoEndpoint.info();
    }

    /**
     * collect cpu info
     */
    @Scheduled(fixedRate = 1000*10, initialDelay = 1000*10)
    public void collectCpu() {
        Map<String,Object> cpu = new LinkedHashMap<>();
        cpu.put("time", System.currentTimeMillis());
        Double cpuUsage = getMetricValue("system.cpu.usage") * 100;
        cpu.put("usage", cpuUsage);
        if(this.cpu.size() >= 100) {
            this.cpu.remove(0);
        }
        this.cpu.add(cpu);
    }

    /**
     * return cpu info
     * @return cpu info
     */
    @GetMapping("get-cpu")
    @ResponseBody
    public List<Map<String,Object>> getCpu() {
        return cpu;
    }

    /**
     * collect memory info
     */
    @Scheduled(fixedRate = 1000*10, initialDelay = 1000*10)
    public void collectMemory() {
        Map<String,Object> memory = new LinkedHashMap<>();
        memory.put("time", System.currentTimeMillis());
        Double max = getMetricValue("jvm.memory.max")/1024/1024;
        Double used = getMetricValue("jvm.memory.used")/1024/1024;
        memory.put("max", max);
        memory.put("used", used);
        if(this.memory.size() >= 100) {
            this.memory.remove(0);
        }
        this.memory.add(memory);
    }

    /**
     * returns memory info
     * @return memory info
     */
    @GetMapping("get-memory")
    @ResponseBody
    public List<Map<String,Object>> getMemory() {
        return memory;
    }

    /**
     * collect disk info
     */
    @Scheduled(fixedRate = 1000*60, initialDelay = 1000*10)
    public void collectDisk() {
        Map<String,Object> disk = new LinkedHashMap<>();
        disk.put("time", System.currentTimeMillis());
        Double total = getMetricValue("disk.total")/1014/1024/1024;
        Double free = getMetricValue("disk.free")/1024/1024/1024;
        Double used = total - free;
        disk.put("total", total);
        disk.put("used", used);
        disk.put("free", free);
        if(this.disk.size() >= 100) {
            this.disk.remove(0);
        }
        this.disk.add(disk);
    }

    /**
     * returns disk info
     * @return disk info
     */
    @GetMapping("get-disk")
    @ResponseBody
    public List<Map<String,Object>> getDisk() {
        return disk;
    }

    /**
     * get metric value
     * @param name metric name
     * @return metric value
     */
    private Double getMetricValue (String name) {
        return metricsEndpoint.metric(name, null)
                .getMeasurements()
                .stream()
                .findFirst()
                .map(MetricsEndpoint.Sample::getValue)
                .orElse(null);
    }

}
