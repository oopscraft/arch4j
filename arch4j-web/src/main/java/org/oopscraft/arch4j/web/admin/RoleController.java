package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/role")
@PreAuthorize("hasAuthority('ADMIN_ROLE')")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService securityService;

    private final AuthorityService authorityService;

    @GetMapping
    public ModelAndView role() {
        return new ModelAndView("admin/role.html");
    }

    @GetMapping("get-roles")
    @ResponseBody
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        return securityService.getRoles(roleSearch, pageable);
    }

    @GetMapping("get-role")
    @ResponseBody
    public Role getRole(@RequestParam("roleId")String roleId) {
        return securityService.getRole(roleId)
                .orElseThrow();
    }

    @PostMapping("save-role")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_ROLE_EDIT')")
    public Role saveRole(@RequestBody @Valid Role role) {
        return securityService.saveRole(role);
    }

    @GetMapping("delete-role")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_ROLE_EDIT')")
    public void deleteRole(@RequestParam("roleId") String roleId) {
        securityService.deleteRole(roleId);
    }

    @GetMapping("get-authorities")
    @ResponseBody
    public Page<Authority> getAuthorities(AuthoritySearch authoritySearch, Pageable pageable) {
        return authorityService.getAuthorities(authoritySearch, pageable);
    }

    @GetMapping("get-authority")
    @ResponseBody
    public Authority getAuthority(@RequestParam("authorityId") String authorityId) {
        return authorityService.getAuthority(authorityId)
                .orElseThrow();
    }

    @PostMapping("save-authority")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_ROLE_EDIT')")
    public Authority saveAuthority(@RequestBody @Valid Authority authority) {
        return authorityService.saveAuthority(authority);
    }

    @GetMapping("delete-authority")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_ROLE_EDIT')")
    public void deleteAuthority(@RequestParam("authorityId")String authorityId) {
        authorityService.deleteAuthority(authorityId);
    }

}
