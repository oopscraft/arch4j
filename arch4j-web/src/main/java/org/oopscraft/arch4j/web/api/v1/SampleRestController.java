package org.oopscraft.arch4j.web.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.sample.Sample;
import org.oopscraft.arch4j.core.sample.SampleSearch;
import org.oopscraft.arch4j.core.sample.SampleService;
import org.oopscraft.arch4j.core.sample.SampleType;
import org.oopscraft.arch4j.web.api.v1.dto.SampleResponse;
import org.oopscraft.arch4j.web.support.PageableAsQueryParam;
import org.oopscraft.arch4j.web.support.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/v1/sample")
@RequiredArgsConstructor
public class SampleRestController {

    private final SampleService sampleService;

    @GetMapping
    @Operation(summary = "Gets list of samples")
    @PageableAsQueryParam
    public ResponseEntity<List<SampleResponse>> getSamples(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            Pageable pageable
    ) {
        SampleSearch sampleSearch = SampleSearch.builder()
                .id(id)
                .name(name)
                .type(Optional.ofNullable(type).map(SampleType.valueOf(type)).orElse(null))
                .build();
        Page<Sample> samplePage = sampleService.getSamplesByJpa(sampleSearch, pageable);
        List<Sample> samples = samplePage.getContent();
        long totalSize = samplePage.getTotalElements();

        return ResponseEntity.ok()
                .contentType(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("sample", pageable, totalSize))
                .body(samples);
    }

}
