package org.oopscraft.arch4j.web.api.v1.login.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueVerificationResponse {

    private String issueId;

    private LocalDateTime issuedAt;

}
