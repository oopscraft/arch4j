package org.chomookun.arch4j.web.login.api.v1.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginRequest {

    private String username;

    private String password;

}
