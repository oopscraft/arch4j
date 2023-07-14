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

    private final AuthenticationTokenService accessTokenEncoder;

    private final UserLoginService userLoginService;

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

    @PostMapping("change-password")
    @ResponseBody
    public void changePassword(@RequestBody Map<String,String> payload) {
        userService.changePassword(payload.get("userId"), payload.get("password"));
    }

    @GetMapping("generate-access-token")
    @ResponseBody
    public String generateAccessToken(@RequestParam("userId")String userId) {
        User user = userService.getUser(userId).orElseThrow();
        UserDetailsImpl userDetails = UserDetailsImpl.from(user);
        return accessTokenEncoder.encodeAuthenticationToken(userDetails);
    }

    @GetMapping("get-user-logins")
    @ResponseBody
    public Page<UserLogin> getUserLogins(UserLoginSearch userLoginSearch, Pageable pageable) {
        return userLoginService.getUserLogins(userLoginSearch, pageable);
    }

}
