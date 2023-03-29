package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.entity.UserEntity;
import org.oopscraft.arch4j.core.user.UserSearch;
import org.oopscraft.arch4j.core.user.UserService;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/user.html");
    }

    @PostMapping("save-user")
    public void saveUser(User user) {
        userService.saveUser(user);
    }

    @GetMapping("get-users")
    @ResponseBody
    public Page<User> getUsers(UserSearch userSearch, Pageable pageable) {
        return userService.getUsers(userSearch, pageable);
    }

    @GetMapping("get-user")
    @ResponseBody
    public User getUser(@RequestParam("id") String id) {
        return userService.getUser(id).orElseThrow(() -> new DataNotFoundException(id));
    }

    @GetMapping("delete-user")
    public void deleteUser(@RequestParam("id") String id) {
        userService.deleteUser(id);
    }

}
