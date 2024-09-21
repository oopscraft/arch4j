package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.security.model.Role;
import org.oopscraft.arch4j.core.security.model.RoleSearch;
import org.oopscraft.arch4j.core.security.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin/common")
@PreAuthorize("hasAuthority('admin.common')")
@RequiredArgsConstructor
public class CommonController {

    private final RoleService roleService;

    @GetMapping("get-roles")
    @ResponseBody
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        return roleService.getRoles(roleSearch, pageable);
    }

}
