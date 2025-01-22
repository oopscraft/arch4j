package org.chomookun.arch4j.web.user.api.v1.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

    private String userId;

    private String username;

    private String email;

    private String mobile;

    private String photo;

    private String profile;

}
