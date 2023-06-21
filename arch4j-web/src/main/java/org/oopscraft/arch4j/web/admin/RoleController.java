package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.*;
import org.oopscraft.arch4j.core.user.*;
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

    private final RoleService roleService;

    private final AuthorityService authorityService;

    /**
     * page
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/role.html");
        modelAndView.addObject("userTypes", UserType.values());
        modelAndView.addObject("userStatuses", UserStatus.values());
        return modelAndView;
    }

    /**
     * get roles
     * @param roleSearch role search condition
     * @param pageable pageable
     * @return roles
     */
    @GetMapping("get-roles")
    @ResponseBody
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        return roleService.getRoles(roleSearch, pageable);
    }

    /**
     * returns role
     * @param roleId role id
     * @return role
     */
    @GetMapping("get-role")
    @ResponseBody
    public Role getRole(@RequestParam("roleId")String roleId) {
        return roleService.getRole(roleId)
                .orElseThrow();
    }

    /**
     * saves role
     * @param role role
     */
    @PostMapping("save-role")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_ROLE_EDIT')")
    public Role saveRole(@RequestBody @Valid Role role) {
        return roleService.saveRole(role);
    }

    /**
     * deletes role
     * @param roleId role id
     */
    @GetMapping("delete-role")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_ROLE_EDIT')")
    public void deleteRole(@RequestParam("roleId") String roleId) {
        roleService.deleteRole(roleId);
    }

    /**
     * gets authorities
     * @param authoritySearch authority search condition
     * @param pageable pageable
     * @return authorities
     */
    @GetMapping("get-authorities")
    @ResponseBody
    public Page<Authority> getAuthorities(AuthoritySearch authoritySearch, Pageable pageable) {
        return authorityService.getAuthorities(authoritySearch, pageable);
    }

    /**
     * returns authority
     * @param authorityId authority id
     * @return authority info
     */
    @GetMapping("get-authority")
    @ResponseBody
    public Authority getAuthority(@RequestParam("authorityId") String authorityId) {
        return authorityService.getAuthority(authorityId)
                .orElseThrow();
    }

    /**
     * saves authority
     * @param authority authority info
     */
    @PostMapping("save-authority")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_ROLE_EDIT')")
    public Authority saveAuthority(@RequestBody @Valid Authority authority) {
        return authorityService.saveAuthority(authority);
    }

    /**
     * deletes authority
     * @param authorityId authority id
     */
    @GetMapping("delete-authority")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_ROLE_EDIT')")
    public void deleteAuthority(@RequestParam("authorityId")String authorityId) {
        authorityService.deleteAuthority(authorityId);
    }

}
