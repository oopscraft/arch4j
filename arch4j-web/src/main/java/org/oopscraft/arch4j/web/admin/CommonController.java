package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.security.AuthenticationTokenService;
import org.oopscraft.arch4j.core.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin/common")
@RequiredArgsConstructor
public class CommonController {

    private final UserService userService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    private final AuthenticationTokenService accessTokenEncoder;

    @GetMapping("get-roles")
    @ResponseBody
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        return roleService.getRoles(roleSearch, pageable);
    }

}
