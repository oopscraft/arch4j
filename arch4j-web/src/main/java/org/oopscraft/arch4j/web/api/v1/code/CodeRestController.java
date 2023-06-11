package org.oopscraft.arch4j.web.api.v1.code;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.code.Code;
import org.oopscraft.arch4j.core.code.CodeSearch;
import org.oopscraft.arch4j.core.code.CodeService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/code")
@RequiredArgsConstructor
public class CodeRestController {

    private final CodeService codeService;

    /**
     * return list of code
     * @param codeId
     * @param name
     * @param pageable
     * @return
     */
    @GetMapping
    @Operation(summary = "Gets list of CodeEntity", description = "returns code list")
    @PageableAsQueryParam
    public ResponseEntity<List<CodeResponse>> getCodes(
            @RequestParam(value = "codeId", required = false) String codeId,
            @RequestParam(value = "name", required = false) String name,
            Pageable pageable
    ) {
        CodeSearch codeSearch = CodeSearch.builder()
                .codeId(codeId)
                .name(name)
                .build();
        Page<Code> codePage = codeService.getCodes(codeSearch, pageable);
        List<CodeResponse> codeResponses = codePage.stream()
                .map(CodeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("code", pageable, codePage.getTotalElements()))
                .body(codeResponses);
    }

}
