package org.oopscraft.arch4j.web.sample;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.sample.Sample;
import org.oopscraft.arch4j.core.sample.SampleSearch;
import org.oopscraft.arch4j.core.sample.SampleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("sample")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping("get-samples")
    @ResponseBody
    public Page<Sample> getSamples(
            SampleSearch sampleSearch,
            @RequestParam(value = "daoType", required = false) String daoType,
            Pageable pageable
    ) {
        if("MYBATIS".equalsIgnoreCase(daoType)){
            return sampleService.getSamplesByJpa(sampleSearch, pageable);
        }
        if("QUERYDSL".equalsIgnoreCase(daoType)){
            return sampleService.getSamplesByQuerydsl(sampleSearch, pageable);
        }
        return sampleService.getSamplesByJpa(sampleSearch, pageable);
    }

}
