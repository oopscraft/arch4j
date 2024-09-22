package org.oopscraft.arch4j.web.login.api.v1.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

}
