package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.message.Message;
import org.oopscraft.arch4j.core.page.Page;
import org.oopscraft.arch4j.core.page.PageSearch;
import org.oopscraft.arch4j.core.page.PageService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/page")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN_PAGE')")
public class PageController {

    private final PageService pageService;

    /**
     * index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/page.html");
    }

    /**
     * get page list
     * @return variables
     */
    @GetMapping("get-pages")
    @ResponseBody
    public PageImpl<Page> getPages(PageSearch pageSearch, Pageable pageable) {
        return pageService.getPages(pageSearch, pageable);
    }

    /**
     * get page
     * @param pageId page id
     * @return message
     */
    @GetMapping("get-page")
    @ResponseBody
    public Page getPage(@RequestParam("pageId")String pageId) {
        return pageService.getPage(pageId)
                .orElseThrow();
    }

    /**
     * save page
     * @param page page info
     * @return page
     */
    @PostMapping("save-page")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_PAGE_EDIT')")
    public Page savePage(@RequestBody @Valid Page page) {
        return pageService.savePage(page);
    }

    /**
     * delete page
     * @param pageId page id
     */
    @GetMapping("delete-page")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_PAGE_EDIT')")
    public void deletePage(@RequestParam("pageId")String pageId) {
        pageService.deletePage(pageId);
    }

}
