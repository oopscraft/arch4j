package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.*;
import org.oopscraft.arch4j.core.user.*;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.oopscraft.arch4j.web.security.AccessTokenEncoder;
import org.oopscraft.arch4j.web.security.UserDetailsImpl;
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

    private final AccessTokenEncoder accessTokenEncoder;

    private final LoginHistoryService loginHistoryService;

    /**
     * index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/user.html");
        modelAndView.addObject("userTypes", UserType.values());
        modelAndView.addObject("userStatuses", UserStatus.values());
        return modelAndView;
    }

    /**
     * returns list of user
     * @param userSearch user search condition
     * @param pageable pagination info
     * @return list of user
     */
    @GetMapping("get-users")
    @ResponseBody
    public Page<User> getUsers(UserSearch userSearch, Pageable pageable) {
        return userService.getUsers(userSearch, pageable);
    }

    /**
     * returns user info
     * @param id user id
     * @return user info
     */
    @GetMapping("get-user")
    @ResponseBody
    public User getUser(@RequestParam("id") String id) {
        return userService.getUser(id)
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    /**
     * saves user info
     * @param user user info
     */
    @PostMapping("save-user")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_USER_EDIT')")
    public void saveUser(@RequestBody @Valid User user) {
        userService.saveUser(user);
    }

    /**
     * deletes user
     * @param id user id
     */
    @GetMapping("delete-user")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_USER_EDIT')")
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
     * change password
     * @param payload payload
     */
    @PostMapping("change-password")
    @ResponseBody
    public void changePassword(@RequestBody Map<String,String> payload) {
        userService.changePassword(payload.get("id"), payload.get("password"));
    }

    /**
     * generate access token
     * @param id
     */
    @GetMapping("generate-access-token")
    @ResponseBody
    public String generateAccessToken(@RequestParam("id")String id) {
        User user = userService.getUser(id).orElseThrow(() -> new DataNotFoundException(id));
        UserDetailsImpl userDetails = UserDetailsImpl.from(user);
        return accessTokenEncoder.encode(userDetails);
    }

    /**
     * get login history
     * @param loginHistorySearch search condition
     * @param pageable pageable
     * @return list of login history
     */
    @GetMapping("get-login-histories")
    @ResponseBody
    public Page<LoginHistory> getLoginHistories(LoginHistorySearch loginHistorySearch, Pageable pageable) {
        return loginHistoryService.getLoginHistories(loginHistorySearch, pageable);
    }

}
