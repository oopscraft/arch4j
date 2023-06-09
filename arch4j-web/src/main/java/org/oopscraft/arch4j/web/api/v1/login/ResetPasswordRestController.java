package org.oopscraft.arch4j.web.api.v1.login;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.email.EmailService;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.oopscraft.arch4j.web.api.v1.login.dto.ResetPasswordRequest;
import org.oopscraft.arch4j.web.error.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/login/reset-password")
@RequiredArgsConstructor
public class ResetPasswordRestController {

    private final UserService userService;

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {

        // checks already existing user
        User user = userService.getUserByEmail(resetPasswordRequest.getEmail()).orElse(null);
        if(user == null) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("user not found")
                    .build();
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }

        // check answer code
        try {
            emailService.validateEmailVerification(resetPasswordRequest.getEmail(), resetPasswordRequest.getAnswer());
        } catch (Throwable t) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(t.getMessage())
                    .build();
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }

        // update password
        userService.changePassword(user.getUserId(), resetPasswordRequest.getPassword());

        // response
        return ResponseEntity.ok().build();
    }

    @GetMapping("validate-email/{email}")
    public ResponseEntity<?> validateEmail(@PathVariable("email")String email) {

        // check duplicated email
        User user = userService.getUserByEmail(email).orElse(null);
        if(user == null) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("email is not registered.")
                    .build();
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }

        // issue verification
        emailService.issueEmailVerification(email);

        // response
        return ResponseEntity.ok().build();
    }

    @GetMapping("validate-email/{email}/answer/{answer}")
    public ResponseEntity<?> validateEmailAnswer(@PathVariable("email")String email, @PathVariable("answer")String answer) {
        try {
            emailService.validateEmailVerification(email, answer);
        } catch (Throwable t) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(t.getMessage())
                    .build();
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
        return ResponseEntity.ok().build();
    }




}
