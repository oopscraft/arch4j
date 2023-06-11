package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.*;
import org.oopscraft.arch4j.core.user.*;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.oopscraft.arch4j.core.security.AuthenticationTokenService;
import org.oopscraft.arch4j.core.security.UserDetailsImpl;
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

    private final AuthenticationTokenService accessTokenEncoder;

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
     * @param userId user id
     * @return user info
     */
    @GetMapping("get-user")
    @ResponseBody
    public User getUser(@RequestParam("userId") String userId) {
        return userService.getUser(userId)
                .orElseThrow(() -> new DataNotFoundException(userId));
    }

    /**
     * saves user info
     * @param user user info
     */
    @PostMapping("save-user")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_USER_EDIT')")
    public User saveUser(@RequestBody @Valid User user) {
        return userService.saveUser(user);
    }

    /**
     * deletes user
     * @param userId user id
     */
    @GetMapping("delete-user")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_USER_EDIT')")
    public void deleteUser(@RequestParam("userId") String userId) {
        userService.deleteUser(userId);
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
        userService.changePassword(payload.get("userId"), payload.get("password"));
    }

    /**
     * generate access token
     * @param userId
     */
    @GetMapping("generate-access-token")
    @ResponseBody
    public String generateAccessToken(@RequestParam("userId")String userId) {
        User user = userService.getUser(userId).orElseThrow(() -> new DataNotFoundException(userId));
        UserDetailsImpl userDetails = UserDetailsImpl.from(user);
        return accessTokenEncoder.encodeAuthenticationToken(userDetails);
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
