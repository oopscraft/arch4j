package org.oopscraft.arch4j.web.api.v1.user.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrentUserRequest {

    private String userName;

    private String email;

    private String mobile;

    private String photo;

    private String profile;

}
