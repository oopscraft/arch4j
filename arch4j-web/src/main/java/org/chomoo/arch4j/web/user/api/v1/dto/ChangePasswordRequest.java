package org.chomoo.arch4j.web.user.api.v1.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

}
