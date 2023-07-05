package org.oopscraft.arch4j.web.api.v1.page;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.page.Page;
import org.oopscraft.arch4j.core.page.PageService;
import org.oopscraft.arch4j.web.api.v1.page.dto.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/page")
@RequiredArgsConstructor
public class PageRestController {

    private final PageService pageService;

    /**
     * return page
     * @param pageId page id
     * @return page response
     */
    @GetMapping("{pageId}")
    public ResponseEntity<PageResponse> getPage(@PathVariable("pageId")String pageId) {
        Page page = pageService.getPage(pageId).orElseThrow();
        PageResponse pageResponse = PageResponse.from(page);
        return ResponseEntity.ok().body(pageResponse);
    }

}
