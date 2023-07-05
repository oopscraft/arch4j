package org.oopscraft.arch4j.web.api.v1.login.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueVerificationRequest {

    private String email;

}
