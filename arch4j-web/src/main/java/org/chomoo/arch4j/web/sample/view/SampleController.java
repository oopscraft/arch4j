package org.chomoo.arch4j.web.sample.view;

import lombok.RequiredArgsConstructor;
import org.chomoo.arch4j.core.sample.model.Sample;
import org.chomoo.arch4j.core.sample.model.SampleSearch;
import org.chomoo.arch4j.core.sample.service.SampleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("sample")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("sample.html");
    }

    @PostMapping("save-sample")
    @ResponseBody
    public Sample saveSample(@RequestBody Sample sample) {
        return sampleService.saveSample(sample);
    }

    /**
     * get sample
     * @param sampleId sample id
     * @return sample
     */
    @GetMapping("get-sample")
    @ResponseBody
    public Sample getSample(@RequestParam("sampleId") String sampleId) {
        return sampleService.getSample(sampleId)
                .orElseThrow();
    }

    /**
     * delete sample
     * @param sampleId sample id
     */
    @GetMapping("delete-sample")
    @ResponseBody
    public void deleteSample(@RequestParam("sampleId") String sampleId) {
        sampleService.deleteSample(sampleId);
    }

    /**
     * search samples
     * @param sampleSearch search condition
     * @param daoType dao type
     * @param pageable pageable
     * @return sample page
     */
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
