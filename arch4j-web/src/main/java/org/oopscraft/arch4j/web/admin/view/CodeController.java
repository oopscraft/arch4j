package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.code.Code;
import org.oopscraft.arch4j.core.code.CodeSearch;
import org.oopscraft.arch4j.core.code.CodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin/code")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN_CODE')")
public class CodeController {

    private final CodeService codeService;

    @GetMapping
    public ModelAndView code() {
        return new ModelAndView("admin/code.html");
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
    @PreAuthorize("hasAuthority('ADMIN_CODE_EDIT')")
    @Transactional
    public Code saveCode(@RequestBody Code code) {
        return codeService.saveCode(code);
    }

    @GetMapping("delete-code")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_CODE_EDIT')")
    @Transactional
    public void deleteCode(@RequestParam("codeId")String codeId) {
        codeService.deleteCode(codeId);
    }

}
