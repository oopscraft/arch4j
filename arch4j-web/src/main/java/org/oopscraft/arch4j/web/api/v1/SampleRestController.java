package org.oopscraft.arch4j.web.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import org.oopscraft.arch4j.web.api.v1.dto.SampleResponse;
import org.oopscraft.arch4j.web.support.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api/v1/sample")
public class SampleRestController {

    @GetMapping
    @Operation(summary = "Gets list of samples")
    @PageableAsQueryParam
    public ResponseEntity<List<SampleResponse>> getSamples(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            Pageable pageable
    ) {

        return ResponseEntity.ok()
                .body(new ArrayList<SampleResponse>());
    }

}
