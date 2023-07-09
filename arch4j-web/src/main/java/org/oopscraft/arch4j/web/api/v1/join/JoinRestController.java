package org.oopscraft.arch4j.web.api.v1.join;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.email.EmailService;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.oopscraft.arch4j.core.user.UserStatus;
import org.oopscraft.arch4j.core.user.UserType;
import org.oopscraft.arch4j.web.api.v1.join.dto.JoinRequest;
import org.oopscraft.arch4j.web.error.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;

@RestController
@RequestMapping("api/v1/join")
@RequiredArgsConstructor
public class JoinRestController {

    private final UserService userService;

    private final EmailService emailService;

    private final SpringTemplateEngine templateEngine;

    /**
     * join
     * @param joinRequest join request
     */
    @PostMapping
    public ResponseEntity<?> join(@RequestBody JoinRequest joinRequest) {
        User user = User.builder()
                .userId(joinRequest.getUserId())
                .userName(joinRequest.getName())
                .email(joinRequest.getEmail())
                .password(joinRequest.getPassword())
                .type(UserType.GENERAL)
                .status(UserStatus.ACTIVE)
                .build();
        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("validate-user-id/{userId}")
    public ResponseEntity<?> validateUserId(@PathVariable("userId")String userId) {
        User user = userService.getUser(userId).orElse(null);
        if(user != null) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpStatus.CONFLICT)
                    .message("duplicated user id")
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("validate-email/{email}")
    public ResponseEntity<?> validateEmail(@PathVariable("email")String email) {

        // check duplicated email
        User user = userService.getUserByEmail(email).orElse(null);
        if(user != null) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpStatus.CONFLICT)
                    .message("duplicated email")
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        // issue verification
        emailService.issueEmailVerification(email);

        // response
        return ResponseEntity.ok().build();
    }

    /**
     * validate email verification answer
     * @param email email
     * @param answer answer
     * @return response
     */
    @GetMapping("validate-email/{email}/check-answer/{answer}")
    public ResponseEntity<?> checkVerification(@PathVariable("email")String email, @PathVariable("answer")String answer) {
        try {
            emailService.checkEmailVerification(email, answer);
        } catch (Throwable t) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message(t.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        return ResponseEntity.ok().build();
    }


}
