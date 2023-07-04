package org.oopscraft.arch4j.core.verification.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "core_verification")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationEntity extends SystemFieldEntity {

    @Id
    @Column(name = "issue_id", length = 32)
    private String issueId;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "answer", length = 32)
    private String answer;

}
