package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.*;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    @GetMapping
    public ModelAndView index() {
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
    public User getUser(@RequestParam("id") String id) {
        return userService.getUser(id)
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    @PostMapping("save-user")
    @ResponseBody
    public void saveUser(@RequestBody @Valid User user) {
        userService.saveUser(user);
    }

    @GetMapping("delete-user")
    @ResponseBody
    public void deleteUser(@RequestParam("id") String id) {
        userService.deleteUser(id);
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
     * @param id role id
     * @return role
     */
    @GetMapping("get-role")
    @ResponseBody
    public Role getRole(@RequestParam("id")String id) {
        return roleService.getRole(id)
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    /**
     * saves role
     * @param role role
     */
    @PostMapping("save-role")
    @ResponseBody
    public void saveRole(@RequestBody @Valid Role role) {
        roleService.saveRole(role);
    }

    /**
     * deletes role
     * @param id role id
     */
    @GetMapping("delete-role")
    @ResponseBody
    public void deleteRole(@RequestParam("id") String id) {
        roleService.deleteRole(id);
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

    @GetMapping("get-authority")
    @ResponseBody
    public Authority getAuthority(@RequestParam("id") String id) {
        return authorityService.getAuthority(id)
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    @PostMapping("save-authority")
    @ResponseBody
    public void saveAuthority(@RequestBody @Valid Authority authority) {
        authorityService.saveAuthority(authority);
    }

    @GetMapping("delete-authority")
    @ResponseBody
    public void deleteAuthority(@RequestParam("id")String id) {
        authorityService.deleteAuthority(id);
    }

}
