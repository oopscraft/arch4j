package org.oopscraft.arch4j.web.login.api.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.email.service.EmailService;
import org.oopscraft.arch4j.web.common.error.ErrorResponse;
import org.oopscraft.arch4j.core.security.model.User;
import org.oopscraft.arch4j.core.security.service.UserService;
import org.oopscraft.arch4j.web.login.api.v1.dto.ResetPasswordRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/login/reset-password")
@RequiredArgsConstructor
@Tag(name = "login")
public class ResetPasswordRestController {

    private final UserService userService;

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {

        // checks already existing user
        User user = userService.getUserByUsername(resetPasswordRequest.getUsername()).orElse(null);
        if(user == null) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("user not found")
                    .build();
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }

        // check answer code
        try {
            emailService.validateEmailVerification(resetPasswordRequest.getUsername(), resetPasswordRequest.getAnswer());
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
        User user = userService.getUserByUsername(email).orElse(null);
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
