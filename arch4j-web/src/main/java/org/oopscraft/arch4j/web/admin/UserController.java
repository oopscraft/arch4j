package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.security.AuthenticationTokenService;
import org.oopscraft.arch4j.core.security.UserDetailsImpl;
import org.oopscraft.arch4j.core.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("admin/user")
@PreAuthorize("hasAuthority('ADMIN_USER')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    private final AuthenticationTokenService accessTokenEncoder;

    @GetMapping
    public ModelAndView user() {
        ModelAndView modelAndView = new ModelAndView("admin/user.html");
        modelAndView.addObject("userTypes", UserType.values());
        modelAndView.addObject("userStatuses", UserStatus.values());
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
    @PreAuthorize("hasAuthority('ADMIN_USER_EDIT')")
    public User saveUser(@RequestBody @Valid User user) {
        return userService.saveUser(user);
    }

    @GetMapping("delete-user")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_USER_EDIT')")
    public void deleteUser(@RequestParam("userId") String userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("change-user-password")
    @ResponseBody
    public void changeUserPassword(@RequestBody Map<String,String> payload) {
        userService.changePassword(payload.get("userId"), payload.get("password"));
    }

    @GetMapping("generate-user-access-token")
    @ResponseBody
    public String generateUserAccessToken(@RequestParam("userId")String userId) {
        User user = userService.getUser(userId).orElseThrow();
        UserDetailsImpl userDetails = UserDetailsImpl.from(user);
        return accessTokenEncoder.encodeAuthenticationToken(userDetails);
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
    @PreAuthorize("hasAuthority('ADMIN_USER_EDIT')")
    public Role saveRole(@RequestBody @Valid Role role) {
        return roleService.saveRole(role);
    }

    @GetMapping("delete-role")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_USER_EDIT')")
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
    @PreAuthorize("hasAuthority('ADMIN_USER_EDIT')")
    public Authority saveAuthority(@RequestBody @Valid Authority authority) {
        return authorityService.saveAuthority(authority);
    }

    @GetMapping("delete-authority")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_USER_EDIT')")
    public void deleteAuthority(@RequestParam("authorityId")String authorityId) {
        authorityService.deleteAuthority(authorityId);
    }

}
