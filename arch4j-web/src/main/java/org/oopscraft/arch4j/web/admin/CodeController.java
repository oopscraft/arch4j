package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.code.Code;
import org.oopscraft.arch4j.core.code.CodeSearch;
import org.oopscraft.arch4j.core.code.CodeService;
import org.oopscraft.arch4j.web.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin/code")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    /**
     * index
     * @return
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/code.html");
    }

    /**
     * get code
     * @return code list
     */
    @GetMapping("get-codes")
    @ResponseBody
    public Page<Code> getCodes(CodeSearch codeSearch, Pageable pageable) {
        return codeService.getCodes(codeSearch, pageable);
    }

    /**
     * get property
     * @param id
     * @return
     */
    @GetMapping("get-code")
    public Code getCode(@RequestParam("id")String id) {
        return codeService.getCode(id).orElseThrow(()->new NotFoundException(id));
    }

}
