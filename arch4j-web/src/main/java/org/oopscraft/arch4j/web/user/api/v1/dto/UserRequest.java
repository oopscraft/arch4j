package org.oopscraft.arch4j.web.user.api.v1.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

    private String userName;

    private String email;

    private String mobile;

    private String photo;

    private String profile;

}
