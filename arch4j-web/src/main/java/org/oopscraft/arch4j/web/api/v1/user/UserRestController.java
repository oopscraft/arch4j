package org.oopscraft.arch4j.web.api.v1.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.web.error.ErrorResponse;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.oopscraft.arch4j.web.api.v1.user.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Tag(name = "user", description = "User")
public class UserRestController {

    private final UserService userService;

    @GetMapping("{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId")String userId) {
        UserResponse userResponse = userService.getUser(userId)
                .map(UserResponse::from)
                .orElseThrow();
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> modifyUser(@PathVariable("userId")String userId, @RequestBody UserRequest userRequest) {
        String currentUserId = SecurityUtils.getCurrentUserId();
        if(!userId.equals(currentUserId)) {
            throw new AccessDeniedException("Not login user");
        }
        User currentUser = userService.getUser(currentUserId).orElseThrow();
        currentUser.setUserName(userRequest.getUserName());
        currentUser.setEmail(userRequest.getEmail());
        currentUser.setMobile(userRequest.getMobile());
        currentUser.setPhoto(userRequest.getPhoto());
        currentUser.setProfile(userRequest.getProfile());
        currentUser = userService.saveUser(currentUser);
        return ResponseEntity.ok(UserResponse.from(currentUser));
    }

    @PutMapping("{userId}/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changeUserPassword(
            @PathVariable("userId")String userId,
            @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        String currentUserId = SecurityUtils.getCurrentUserId();
        if(!userId.equals(currentUserId)) {
            throw new AccessDeniedException("Not login user");
        }
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

}
