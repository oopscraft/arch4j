package org.chomoo.arch4j.web.join.api.v1.dto;

import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "User Name is required")
    private String userName;

    @NotBlank(message = "#{ 'web.join.email' }")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "web.join.join.answer")
    private String answer;

    @NotBlank(message = "Password is required")
    private String password;

}
