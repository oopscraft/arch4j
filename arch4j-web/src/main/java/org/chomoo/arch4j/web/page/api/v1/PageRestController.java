package org.chomoo.arch4j.web.page.api.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomoo.arch4j.core.page.model.Page;
import org.chomoo.arch4j.core.page.service.PageService;
import org.chomoo.arch4j.web.page.api.v1.dto.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/page")
@RequiredArgsConstructor
@Tag(name = "page", description = "Page")
public class PageRestController {

    private final PageService pageService;

    @GetMapping("{pageId}")
    public ResponseEntity<PageResponse> getPage(@PathVariable("pageId")String pageId) {
        Page page = pageService.getPage(pageId).orElseThrow();
        PageResponse pageResponse = PageResponse.from(page);
        return ResponseEntity.ok().body(pageResponse);
    }

}
