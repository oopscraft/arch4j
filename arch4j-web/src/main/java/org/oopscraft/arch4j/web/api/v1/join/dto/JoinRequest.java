package org.oopscraft.arch4j.web.api.v1.join.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
