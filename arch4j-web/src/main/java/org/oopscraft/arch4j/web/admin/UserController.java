package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.*;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

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

    @GetMapping("get-roles")
    @ResponseBody
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        return roleService.getRoles(roleSearch, pageable);
    }

}
