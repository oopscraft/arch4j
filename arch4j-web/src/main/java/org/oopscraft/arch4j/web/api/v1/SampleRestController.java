package org.oopscraft.arch4j.web.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/sample")
@RequiredArgsConstructor
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
                .id(id)
                .name(name)
                .type(Optional.ofNullable(type).map(SampleType::valueOf).orElse(null))
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
