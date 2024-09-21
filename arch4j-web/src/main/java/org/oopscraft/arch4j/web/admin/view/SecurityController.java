package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.security.service.AuthorityService;
import org.oopscraft.arch4j.core.security.service.RoleService;
import org.oopscraft.arch4j.core.security.model.*;
import org.oopscraft.arch4j.core.security.service.UserService;
import org.oopscraft.arch4j.web.security.service.SecurityTokenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("admin/security")
@PreAuthorize("hasAuthority('admin.security')")
@RequiredArgsConstructor
public class SecurityController {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final SecurityTokenService securityTokenService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    @GetMapping
    public ModelAndView security() {
        ModelAndView modelAndView = new ModelAndView("admin/security.html");
        modelAndView.addObject("userStatuses", User.Status.values());
        return modelAndView;
    }

    @GetMapping("get-users")
    @ResponseBody
    public Page<User> getUsers(UserSearch userSearch, Pageable pageable) {
        return userService.getUsers(userSearch, pageable);
    }

    @GetMapping("get-user")
    @ResponseBody
    public User getUser(@RequestParam("userId") String userId) {
        return userService.getUser(userId).orElseThrow();
    }

    @PostMapping("save-user")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.security.edit')")
    public User saveUser(@RequestBody @Valid User user) {
        return userService.saveUser(user);
    }

    @GetMapping("delete-user")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.security.edit')")
    public void deleteUser(@RequestParam("userId") String userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("change-user-password")
    @ResponseBody
    public void changeUserPassword(@RequestBody Map<String,String> payload) {
        userService.changePassword(payload.get("userId"), payload.get("password"));
    }

    @PostMapping(value = "generate-security-token", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.security.edit')")
    public String generateAccessToken(@RequestBody Map<String,String> payload) {
        String userId = Optional.ofNullable(payload.get("userId")).orElseThrow();
        int expirationDays = Optional.ofNullable(payload.get("expirationDays")).map(Integer::parseInt).orElseThrow();
        int expirationMinutes = expirationDays * 60 * 24;
        User user = userService.getUser(userId).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return securityTokenService.encodeSecurityToken(userDetails, expirationMinutes);
    }

    @GetMapping("get-roles")
    @ResponseBody
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        return roleService.getRoles(roleSearch, pageable);
    }

    @GetMapping("get-role")
    @ResponseBody
    public Role getRole(@RequestParam("roleId")String roleId) {
        return roleService.getRole(roleId)
                .orElseThrow();
    }

    @PostMapping("save-role")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.security.edit')")
    public Role saveRole(@RequestBody @Valid Role role) {
        return roleService.saveRole(role);
    }

    @GetMapping("delete-role")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.security.edit')")
    public void deleteRole(@RequestParam("roleId") String roleId) {
        roleService.deleteRole(roleId);
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
    @PreAuthorize("hasAuthority('admin.security.edit')")
    public Authority saveAuthority(@RequestBody @Valid Authority authority) {
        return authorityService.saveAuthority(authority);
    }

    @GetMapping("delete-authority")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.security.edit')")
    public void deleteAuthority(@RequestParam("authorityId")String authorityId) {
        authorityService.deleteAuthority(authorityId);
    }

}
