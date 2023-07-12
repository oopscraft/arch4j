package org.oopscraft.arch4j.web.api.v1.user;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.oopscraft.arch4j.web.api.v1.user.dto.ChangePasswordRequest;
import org.oopscraft.arch4j.web.api.v1.user.dto.CurrentUserRequest;
import org.oopscraft.arch4j.web.api.v1.user.dto.CurrentUserResponse;
import org.oopscraft.arch4j.web.api.v1.user.dto.UserResponse;
import org.oopscraft.arch4j.web.error.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("current-user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCurrentUser() {
        String currentUserId = SecurityUtils.getCurrentUserId();
        CurrentUserResponse currentUserResponse = userService.getUser(currentUserId)
                .map(CurrentUserResponse::from)
                .orElseThrow();
        return ResponseEntity.ok(currentUserResponse);
    }

    @PutMapping("current-user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> modifyCurrentUser(@RequestBody CurrentUserRequest currentUserRequest) {
        String currentUserId = SecurityUtils.getCurrentUserId();
        User currentUser = userService.getUser(currentUserId).orElseThrow();
        currentUser.setUserName(currentUserRequest.getUserName());
        currentUser.setEmail(currentUserRequest.getEmail());
        currentUser.setMobile(currentUserRequest.getMobile());
        currentUser.setPhoto(currentUserRequest.getPhoto());
        currentUser.setProfile(currentUserRequest.getProfile());
        currentUser = userService.saveUser(currentUser);
        return ResponseEntity.ok(CurrentUserResponse.from(currentUser));
    }

    @PostMapping("current-user/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        String currentUserId = SecurityUtils.getCurrentUserId();
        if(userService.isPasswordMatched(currentUserId, changePasswordRequest.getCurrentPassword())){
            userService.changePassword(currentUserId, changePasswordRequest.getNewPassword());
        }else{
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("password not matched")
                    .build();
            return ResponseEntity
                    .status(errorResponse.getStatus())
                    .body(errorResponse);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId")String userId) {
        UserResponse userResponse = userService.getUser(userId)
                .map(UserResponse::from)
                .orElseThrow();
        return ResponseEntity.ok(userResponse);
    }

}
