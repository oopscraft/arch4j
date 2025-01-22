package org.chomookun.arch4j.web.sample.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.chomookun.arch4j.core.sample.model.Sample;
import org.chomookun.arch4j.core.sample.model.SampleSearch;
import org.chomookun.arch4j.core.sample.service.SampleService;
import org.chomookun.arch4j.core.sample.model.SampleType;
import org.chomookun.arch4j.web.sample.api.v1.dto.SampleResponse;
import org.chomookun.arch4j.web.common.data.PageableAsQueryParam;
import org.chomookun.arch4j.web.common.data.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/sample")
@RequiredArgsConstructor
@Tag(name = "sample", description = "Sample")
public class SampleRestController {

    private final SampleService sampleService;

    private final ModelMapper modelMapper;

    @GetMapping
    @Operation(summary = "Gets list of samples")
    @PageableAsQueryParam
    public ResponseEntity<List<SampleResponse>> getSamples(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            Pageable pageable
    ) {
        // search condition
        SampleSearch sampleSearch = SampleSearch.builder()
                .sampleId(id)
                .sampleName(name)
                .sampleType(Optional.ofNullable(type).map(SampleType::valueOf).orElse(null))
                .build();

        // search sample page
        Page<Sample> samplePage = sampleService.getSamplesByJpa(sampleSearch, pageable);

        // response list
        List<SampleResponse> sampleResponses = samplePage.getContent().stream()
                .map(sample -> modelMapper.map(sample, SampleResponse.class))
                .collect(Collectors.toList());

        // total size
        long totalSize = samplePage.getTotalElements();

        // response
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("sample", pageable, totalSize))
                .body(sampleResponses);
    }

}
