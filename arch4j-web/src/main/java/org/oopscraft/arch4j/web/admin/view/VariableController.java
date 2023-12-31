package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.variable.Variable;
import org.oopscraft.arch4j.core.variable.VariableSearch;
import org.oopscraft.arch4j.core.variable.VariableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/variable")
@PreAuthorize("hasAuthority('ADMIN_VARIABLE')")
@RequiredArgsConstructor
public class VariableController {

    private final VariableService variableService;

    @GetMapping
    public ModelAndView variable() {
        return new ModelAndView("admin/variable.html");
    }

    @GetMapping("get-variables")
    @ResponseBody
    public Page<Variable> getVariables(VariableSearch variableSearch, Pageable pageable) {
        return variableService.getVariables(variableSearch, pageable);
    }

    @GetMapping("get-variable")
    @ResponseBody
    public Variable getVariable(@RequestParam("variableId")String variableId) {
        return variableService.getVariable(variableId)
                .orElseThrow();
    }

    @PostMapping("save-variable")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_VARIABLE_EDIT')")
    public Variable saveVariable(@RequestBody @Valid Variable variable) {
        return variableService.saveVariable(variable);
    }

    @GetMapping("delete-variable")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_VARIABLE_EDIT')")
    public void deleteVariable(@RequestParam("variableId")String variableId) {
        variableService.deleteVariable(variableId);
    }

}
