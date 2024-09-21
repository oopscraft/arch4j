package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.code.model.Code;
import org.oopscraft.arch4j.core.code.model.CodeSearch;
import org.oopscraft.arch4j.core.code.service.CodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin/codes")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('admin.codes')")
public class CodesController {

    private final CodeService codeService;

    @GetMapping
    public ModelAndView codes() {
        return new ModelAndView("admin/codes.html");
    }

    @GetMapping("get-codes")
    @ResponseBody
    public Page<Code> getCodes(CodeSearch codeSearch, Pageable pageable) {
        return codeService.getCodes(codeSearch, pageable);
    }

    @GetMapping("get-code")
    @ResponseBody
    public Code getCode(@RequestParam("codeId")String codeId) {
        return codeService.getCode(codeId)
                .orElseThrow();
    }

    @PostMapping("save-code")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.codes.edit')")
    @Transactional
    public Code saveCode(@RequestBody Code code) {
        return codeService.saveCode(code);
    }

    @GetMapping("delete-code")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.codes.edit')")
    @Transactional
    public void deleteCode(@RequestParam("codeId")String codeId) {
        codeService.deleteCode(codeId);
    }

}
