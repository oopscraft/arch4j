package org.oopscraft.arch4j.web.api.v1.join.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinRequest {

    private String userId;

    private String name;

    private String email;

    private String answer;

    private String password;

}
