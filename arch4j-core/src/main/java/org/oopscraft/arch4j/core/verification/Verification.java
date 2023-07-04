package org.oopscraft.arch4j.core.verification;

import lombok.*;
import org.oopscraft.arch4j.core.verification.dao.VerificationEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Verification {

    private String issueId;

    private LocalDateTime issuedAt;

    private String answer;

    public static Verification from(VerificationEntity verificationEntity) {
        return Verification.builder()
                .issueId(verificationEntity.getIssueId())
                .issuedAt(verificationEntity.getIssuedAt())
                .answer(verificationEntity.getAnswer())
                .build();
    }

}
